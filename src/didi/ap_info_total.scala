import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.storage.StorageLevel
import org.apache.hadoop.io.compress.GzipCodec
import scala.collection.mutable.ArrayBuffer
import com.didichuxing.map.loc.Utils.{EtlTemplate, JsonUtils}
import com.hadoop.mapreduce.LzoTextInputFormat
import org.apache.hadoop.io.{LongWritable, Text}
import org.apache.spark.rdd.RDD
import scala.util.{Try, Success}

import org.joda.time._
import scala.sys.process._
case class MergePoint(
                       val lng: Int,
                       val lat: Int,
                       val ssid: String,
                       val didiEarly: Long,
                       val didiTime: Long,
                       val baiduEarly: Long,
                       val baiduTime: Long,
                       val level: Array[Int],
                       val latestTime: Long
                     ) {}

case class gatherPoint(
                        val lng: Int,
                        val lat: Int,
                        val didiEarly: Long,
                        val didiTime: Long,
                        val baiduEarly: Long,
                        val baiduTime: Long,
                        val level: Array[Int],
                        val latestTime: Long
                      ) {}

case class collectPoint(
                         val lng: Int,
                         val lat: Int,
                         val level: Array[Int],
                         val earliestTime: Array[Long],
                         val latestTime: Array[Long]
                       ) {}
def levelToHist(level : String): Int = {
  val le = scala.util.Try(level.stripPrefix("'").stripSuffix("'").toInt) match {
    case Success(_) => true
    case _ => false
  }
  var i = 0
  if (le) {
    i = ((Math.abs(level.stripPrefix("'").stripSuffix("'").toInt))-60)/6
  }
  if(i < 0) i = 0
  if(i > 6) i = 6
  if(le == false) i = -1
  i
}

def levelToVertor(level: Int): Array[Int] = {
  var levelVertor = new Array[Int](7)
  if(level >= 0) {
    levelVertor(level) += 1
  }
  levelVertor
}

def collectPointByAp(a: Array[MergePoint], b: Array[MergePoint]): Array[MergePoint] = {
  val res = ArrayBuffer[MergePoint]()
  for(item <- a) {
    res.append(item)
  }
  for(item <- b) {
    res.append(item)
  }
  res.toArray
}

def getEarlyLaterTime(a:(String, Long, Long, Array[Int], Int),
                      b: (String, Long, Long, Array[Int], Int)): (String, Long, Long) = {
  val ear = if(a._2 < b._2) a._2 else b._2
  val (lat, ssid) = if(a._3 < b._3) (b._3, b._1) else (a._3, a._1)
  (ssid, ear, lat)
}

def loadEtlSourceData(sc: SparkContext, input: String, partitionNum: Int, speedThred: Double,
                      sourceType: Int, begin: Long, end: Long): RDD[(String,(String, Long, Long, Array[Int], Int))] = {
  val etlRdd = sc.newAPIHadoopFile(input,
    classOf[LzoTextInputFormat], classOf[LongWritable], classOf[Text])
    .map(p => p._2.toString).repartition(partitionNum)
    .map(line => {
      val cols = line.split("\t")
      JsonUtils.parseJson[EtlTemplate.SrcDataWifi](cols(1))})
    .flatMap(line => Try {
      val w = line.wifiList.map(x => (x.bssid, x.level, x.ssid))
      val l = line.matchGps.get
      Some((l.lng*10000).toInt, (l.lat*10000).toInt, l.speed, l.gpsTime.get/1000, w)
    }.getOrElse(None))
    .filter(t=>t._3.isDefined && t._3.get < speedThred && t._4>= begin && t._4 < end)
    //1:lng 2:lat 3:time 4:(ap, level, ssid)
    .map(t=>(t._1, t._2, t._4, t._5))
    .flatMap(t => {
      //t._4.map(x => (t._1.toString + ","  + t._2.toString + "," + x._1, (x._2, x._3, t._3)))
      t._4.map(x => ((t._1.toString, t._2.toString, x._1), (x._2, x._3, t._3)))
    })
    // ((lng, lat, ap), (level, ssid, time))
    .map(t=>((t._1._1, t._1._2, t._1._3.replace(":", "")), t._2))
    .filter(t=>t._1._3.length == 12)
    .map(t=>(t._1._1 + "," + t._1._2 + "," + t._1._3, t._2))
    // (lng+lat+ap,(ssid, earlyTime, latestTime, level, type))
    .map(t => (t._1, (t._2._2, t._2._3, t._2._3, levelToVertor(levelToHist(t._2._1)), sourceType)))
    .reduceByKey((a,b) => {
      val (ssid, ear, last) = getEarlyLaterTime(a, b)
      val sumLevel = a._4.zip(b._4).map(t=>(t._1 + t._2))
      (ssid, ear, last, sumLevel, sourceType)
    })
  //.persist(StorageLevel.MEMORY_AND_DISK)
  etlRdd
}

/*  def loadBaiduEtlSourceData(sc: SparkContext, input: String, partitionNum: Int, speedThred: Double,
                        sourceType: Int): RDD[(String,(String, Long, Array[Long], Int))] = {*/
def loadBaiduEtlSourceData(sc: SparkContext, input: String, partitionNum: Int, speedThred: Double,
                           sourceType: Int, begin: Long, end: Long):RDD[(String,(String, Long, Long, Array[Int], Int))] = {
  val etlRdd = sc.newAPIHadoopFile(input,
    classOf[LzoTextInputFormat], classOf[LongWritable], classOf[Text])
    .map(p => p._2.toString).repartition(partitionNum)
    .filter(t=>{val sps = t.split("\t")
      if (sps.length !=2) false
      else true})
    .filter(t=>{
      val sps = t.split("\t")
      if (sps(1).contains("wifiList") && sps(1)(sps(1).length-1).toString =="}" && sps(1)(sps(1).length-2).toString == "]")
        true
      else false})
    .map(line => {
      val cols = line.split("\t")
      (cols(0), Try(JsonUtils.parseJson[EtlTemplate.baiduDataInfo](cols(1))))})
    .filter(t=>{t._2.get.wifiList.isDefined && t._2.get.matchGps.isDefined && t._2.get.matchGps.get.gpsTime.isDefined})
    .map(t => t._2.get)
    .flatMap(line => Try {
      val w = line.wifiList.get.map(x => (x.bssid, x.level, ""))
      val l = line.matchGps.get
      Some((l.lng.toDouble*10000).toInt, (l.lat.toDouble*10000).toInt, l.speed, l.gpsTime.get.toLong, w)
    }.getOrElse(None))
    .filter(t => t._3.isDefined && t._3.get.toDouble < speedThred && t._4>= begin && t._4 < end)
    //1:lng 2:lat 3:time 4:(ap, level, ssid)
    .map(t=>(t._1, t._2, t._4, t._5))
    //.repartition(800).saveAsTextFile("/user/map_loc/liruidong/ap_acc/2017/03/26/")
    .flatMap(t => {
    //t._4.map(x => (t._1.toString + ","  + t._2.toString + "," + x._1, (x._2, x._3, t._3)))
    t._4.map(x => ((t._1.toString, t._2.toString, x._1), (x._2, x._3, t._3)))
  })
    // ((lng, lat, ap), (level, ssid, time))
    .map(t=>((t._1._1, t._1._2, t._1._3.replace(":", "")), t._2))
    .filter(t=>t._1._3.length == 12)
    .map(t=>(t._1._1 + "," + t._1._2 + "," + t._1._3, t._2))
    // (lng+lat+ap,(ssid, time, level, type))
    .map(t => (t._1, (t._2._2, t._2._3, t._2._3, levelToVertor(levelToHist(t._2._1.toString)), sourceType)))
    .reduceByKey((a,b) => {
      val (ssid, ear, last) = getEarlyLaterTime(a, b)
      val sumLevel = a._4.zip(b._4).map(t=>(t._1 + t._2))
      (ssid, ear, last, sumLevel, sourceType)
    })
  //.persist(StorageLevel.MEMORY_AND_DISK)
  etlRdd
}

//def loadPoints(line: String): (String, (String, Array[collectPoint]) = {
def loadPoints(line: String) = {
  val sps = line.split("")
  val ap = sps(0)
  val ssid = sps(1)

  var token = true
  var i:Int = 2
  val res = ArrayBuffer[collectPoint]()
  var count = 0
  while(token == true && i < sps.length) {
    val ele = sps(i).split("")
    if (ele.length == 3) {
      val pos = ele(0).split("")
      val lng = pos(0).toInt
      val lat = pos(1).toInt

      val didi = ele(1).split("")
      var level = new Array[Int](14)
      for(i <- 0 until 7) {
        level(i) = didi(i).toInt
      }
      var earliestTime = new Array[Long](2)
      var lastestTime = new Array[Long](2)
      earliestTime(0) = didi(7).toLong
      lastestTime(0) = didi(8).toLong

      val baidu = ele(2).split("")
      for(i <- 7 until 14) {
        level(i) = baidu(i - 7).toInt
      }
      earliestTime(1) = baidu(7).toLong
      lastestTime(1) = baidu(8).toLong

      res.append(collectPoint(lng, lat, level, earliestTime, lastestTime))
      count += 1
      if(count > collectPointNum) { token =  false }
      i += 1
    } else { token = false }
  }
  if(token == false && res.length <= 0) { None }
  else { Some((ap, (ssid, res.toArray.slice(0, collectPointNum)))) }
}

def loadPointsV(line: String) = {
  val sps = line.split("")
  val ap = sps(0)
  val ssid = sps(1)

  var token = true
  var i:Int = 2
  val res = ArrayBuffer[collectPoint]()
  var count = 0
  while(token == true && i < sps.length) {
    val ele = sps(i).split("")
    if (ele.length == 3) {
      val pos = ele(0).split("")
      val lng = pos(0).toInt
      val lat = pos(1).toInt

      val didi = ele(1).split("")
      var level = new Array[Int](14)
      for(i <- 0 until 7) {
        level(i) = didi(i).toInt
      }
      var earliestTime = new Array[Long](2)
      var lastestTime = new Array[Long](2)
      earliestTime(0) = didi(7).toLong
      lastestTime(0) = didi(8).toLong

      val baidu = ele(2).split("")
      for(i <- 7 until 14) {
        level(i) = baidu(i - 7).toInt
      }
      earliestTime(1) = baidu(7).toLong
      lastestTime(1) = baidu(8).toLong

      res.append(collectPoint(lng, lat, level, earliestTime, lastestTime))
      count += 1
      if(count > collectPointNum) { token =  false }
      i += 1
    } else { token = false }
  }
  if(token == false && res.length <= 0) { None }
  else { Some((ap,  (ssid, 1, res.toArray.slice(0, collectPointNum)))) }
}

def loadAllCollectPoints(sc: SparkContext, input: String,
                         partitionNum: Int): RDD[(String, (String, Array[collectPoint]))] = {
  sc.textFile(input).repartition(20000)
    .flatMap(t=>loadPoints(t))
    .repartition(20000)
    .persist(StorageLevel.MEMORY_AND_DISK)
}

def loadAllCollectPointsV(sc: SparkContext, input: String,
                          partitionNum: Int): RDD[(String, (String, Int, Array[collectPoint]))] = {
  sc.textFile(input).repartition(20000)
    .flatMap(t=>loadPointsV(t))
    .repartition(12000)
    .persist(StorageLevel.MEMORY_AND_DISK)
}

def loadAllCollectPointsStr(sc: SparkContext, input: String,
                            partitionNum: Int): RDD[(String, String)] = {
  sc.textFile(input).repartition(20000)
    .map(t=> {
      val ap = t.split("")(0)
      (ap, t)
    })
    .repartition(20000)
    .persist(StorageLevel.DISK_ONLY)
}

def getRandomApKey(ap: String): Int = {
  var baseV = 0
  val d = "0"
  for(i <- 0 until ap.length) {
    baseV = baseV + (ap(i) - d(0))
  }
  baseV
}

def loadAllCollectPointsStrHash(sc: SparkContext, input: String,
                                partitionNum: Int, index: Int, rc: Int, flag: Int): RDD[(String, String)] = {
  sc.textFile(input).repartition(20000)
    .map(t=> {
      val sps = t.split("")
      val len = sps.length
      val ap = sps(0)
      /*val d = "0"
      val hashVal = ap(ap.length - 1) - d(0)
      if(scala.math.abs(hashVal) % rc == index) {
        if (flag == 0) { Some((ap, t)) }
        else { Some(ap, sps.slice(0, len - 1).mkString("")) }
      }
      else { None }*/
      val k = getRandomApKey(ap)
      if(k % rc == index) {
        if (flag == 0) { Some((ap, t)) }
        else { Some(ap, sps.slice(0, len - 1).mkString("")) }
      }
      else {None}
    }).flatMap(t=>t)
    .repartition(15000)
  //.persist(StorageLevel.MEMORY_AND_DISK)
}

def combineEtlData(etlData: Array[(String, Long, Long, Array[Int], Int)]): (String, Long, Long,  Long, Long, Array[Int], Long) = {
  var ssid:String = ""
  var latestime: Long = -1
  var didiTime: Long = -1
  var didi: Long = "9999999999".toLong
  var baiduTime: Long = -1
  var baidu: Long = "9999999999".toLong
  var level = new Array[Int](14)

  for(i <- 0 until etlData.length) {
    if(etlData(i)._3 > latestime) {
      ssid = etlData(i)._1
      latestime = etlData(i)._3
    }
    if(etlData(i)._5 == 1) {
      if(etlData(i)._3 > didiTime) { didiTime = etlData(i)._3 }
      if(etlData(i)._2 < didi) { didi = etlData(i)._2 }
      for(j <- 0 until etlData(i)._4.length) {
        level(j) += etlData(i)._4(j)
      }
    } else if(etlData(i)._5 == 2) {
      if(etlData(i)._3 > baiduTime) { baiduTime = etlData(i)._3 }
      if(etlData(i)._2 < baidu) { baidu = etlData(i)._2 }
      for(j <- 0 until etlData(i)._4.length) {
        level(j+7) += etlData(i)._4(j)
      }
    }
  }
  (ssid, didi, didiTime, baidu, baiduTime, level, latestime)
}

def formatSsid(ssid: String): String = {
  val formatSsid = ssid.replace("", "").replace("", "").replace("", "").replace("\n", "\t").replace("\r", "\t")
  formatSsid
}

def getSsidForPoints(points: Array[MergePoint]): (String, Array[gatherPoint]) = {
  val pot = ArrayBuffer[gatherPoint]()
  val ssid = points(0).ssid
  for(item <- points) {
    pot.append(gatherPoint(item.lng, item.lat, item.didiEarly, item.didiTime, item.baiduEarly, item.baiduTime, item.level, item.latestTime))
  }
  (formatSsid(ssid), pot.toArray)
}

def newSsidForPoints(points: Array[MergePoint]): (String, Long, Array[gatherPoint]) = {
  val pot = ArrayBuffer[gatherPoint]()
  var ssid: String = ""
  var latest: Long = -1

  for(item <- points) {
    if(item.latestTime > latest) {
      ssid = item.ssid
      latest = item.latestTime
    }
    pot.append(gatherPoint(item.lng, item.lat, item.didiEarly, item.didiTime, item.baiduEarly, item.baiduTime, item.level, item.latestTime))
  }
  (formatSsid(ssid), latest, pot.toArray)
}

def newSsidForPointsV(points: Array[MergePoint]): (String, Int, Array[collectPoint]) = {
  val pot = ArrayBuffer[collectPoint]()
  var ssid: String = ""
  var latest: Long = -1

  for(item <- points) {
    if(item.latestTime > latest) {
      ssid = item.ssid
      latest = item.latestTime
    }
    var earlestTime = new Array[Long](2)
    earlestTime(0) = item.didiEarly
    earlestTime(1) = item.baiduEarly
    var lastestTime = new Array[Long](2)
    lastestTime(0) = item.didiTime
    lastestTime(1) = item.baiduTime
    pot.append(collectPoint(item.lng, item.lat, item.level,  earlestTime, lastestTime))
  }
  (formatSsid(ssid) + "," + latest.toString, 2,  pot.toArray)
}

def testInternal0(p: Array[gatherPoint]): String = {
  val res = ArrayBuffer[String]()
  for(i <- p) {
    res.append(i.lng.toString+"|"+i.lat.toString+"|"+i.didiTime.toString+"|"+i.baiduTime.toString+"|"+i.level.mkString("|")+"|"+i.latestTime.toString)
  }
  res.toArray.mkString(",")
}

// Can extend from collectPoint
case class storePoint (
                        val lng: Int,
                        val lat: Int,
                        val level: Array[Int],
                        val earliestTime: Array[Long],
                        val latestTime: Array[Long],
                        val time: Long
                      ) {}

def findPoint(p: gatherPoint, t: Array[collectPoint]): Int = {
  var index:Int = -1
  var i:Int = 0
  var token: Boolean = true
  while(i < t.length && token == true) {
    if(p.lng == t(i).lng && p.lat == t(i).lat) {
      index = i
      token = false
    }
    i += 1
  }
  index
}

def findPointV(p: collectPoint, t: Array[collectPoint]): Int = {
  var index:Int = -1
  var i:Int = 0
  var token: Boolean = true
  while(i < t.length && token == true) {
    if(p.lng == t(i).lng && p.lat == t(i).lat) {
      index = i
      token = false
    }
    i += 1
  }
  index
}

def initAccPoint(p: gatherPoint): storePoint = {
  var lastestTime = new Array[Long](2)
  lastestTime(0) = p.didiTime
  lastestTime(1) = p.baiduTime
  var earlestTime = new Array[Long](2)
  earlestTime(0) = p.didiEarly
  earlestTime(1) = p.baiduEarly
  storePoint(p.lng, p.lat, p.level, earlestTime, lastestTime, p.latestTime)
}

def initAccPointV(p: collectPoint): storePoint = {
  /*var lastestTime = new Array[Long](2)
  lastestTime = p.latestTime
  var earlestTime = new Array[Long](2)
  earlestTime = p.earliestTime*/
  val lt = if(p.latestTime(0) > p.latestTime(1)) p.latestTime(0) else p.latestTime(1)
  storePoint(p.lng, p.lat, p.level, p.earliestTime, p.latestTime, lt)
}

def addTotalAndAcc(p: gatherPoint, t: collectPoint): storePoint = {
  var lastestTime = t.latestTime
  var earlestTime = t.earliestTime
  if(p.didiTime > 0) {
    if(p.didiTime > lastestTime(0)) { lastestTime(0) = p.didiTime }
    //if(earlestTime(0) == -1) { earlestTime(0) = p.didiTime }
    if(p.didiEarly < earlestTime(0)) { earlestTime(0) = p.didiEarly }
    else if(earlestTime(0) == -1) { earlestTime(0) = p.didiEarly }
  }
  if(p.baiduTime > 0) {
    if(p.baiduTime > lastestTime(1)) { lastestTime(1) = p.baiduTime }
    //if(earlestTime(1) == -1) { earlestTime(1) = p.baiduTime }
    if(p.baiduEarly < earlestTime(1)) { earlestTime(1) = p.baiduEarly }
    else if(earlestTime(1) == -1) { earlestTime(1) = p.baiduEarly }
  }

  val lt = if(lastestTime(0) > lastestTime(1)) lastestTime(0) else lastestTime(1)
  val lev = t.level.zip(p.level).map(t=>t._1 + t._2)
  storePoint(p.lng, p.lat, lev, earlestTime, lastestTime, lt)
}

def addTotalAndAccV(p: collectPoint, total: collectPoint): storePoint = {
  var lastestTime = total.latestTime
  var earlestTime = total.earliestTime
  if(p.latestTime(0) > 0) {
    if(p.latestTime(0) > lastestTime(0)) { lastestTime(0) = p.latestTime(0) }
    //if(earlestTime(0) == -1) { earlestTime(0) = p.didiTime }
    if(p.earliestTime(0) < earlestTime(0)) { earlestTime(0) = p.earliestTime(0) }
    else if(earlestTime(0) == -1) { earlestTime(0) = p.earliestTime(0) }
  }
  if(p.latestTime(1) > 0) {
    if(p.latestTime(1) > lastestTime(1)) { lastestTime(1) = p.latestTime(1) }
    //if(earlestTime(1) == -1) { earlestTime(1) = p.baiduTime }
    if(p.earliestTime(1) < earlestTime(1)) { earlestTime(1) = p.earliestTime(1) }
    else if(earlestTime(1) == -1) { earlestTime(1) = p.earliestTime(1) }
  }

  val lt = if(lastestTime(0) > lastestTime(1)) lastestTime(0) else lastestTime(1)
  val lev = total.level.zip(p.level).map(t=>t._1 + t._2)
  storePoint(p.lng, p.lat, lev, earlestTime, lastestTime, lt)
}

def mergeTotalAndAcc(total: (String, Array[collectPoint]),
                     acc:(String, Long, Array[gatherPoint])): (String, Array[collectPoint]) = {
  val didiLastestTime = total._2(0).latestTime(0)
  val baiduLastestTime = total._2(0).latestTime(1)
  val totalLastest = if(didiLastestTime > baiduLastestTime) didiLastestTime else baiduLastestTime
  val ssid = if(totalLastest > acc._2) total._1 else acc._1

  var flagIndex: Set[Int] = Set()
  val res = ArrayBuffer[storePoint]()

  for (item <- acc._3) {
    val index = findPoint(item, total._2)
    if(index != -1) {
      res.append(addTotalAndAcc(item, total._2(index)))
      flagIndex += index
    } else {
      res.append(initAccPoint(item))
    }
  }

  val tt = total._2
  for(i <- 0 until total._2.length) {
    if(!flagIndex.contains(i)) {
      val time = if(tt(i).latestTime(0) > tt(i).latestTime(1)) tt(i).latestTime(0) else tt(i).latestTime(1)
      res.append(storePoint(tt(i).lng, tt(i).lat, tt(i).level, tt(i).earliestTime, tt(i).latestTime, time))
    }
  }

  val rr = res.toArray.sortWith(_.time > _.time).slice(0, collectPointNum)
  val ss = ArrayBuffer[collectPoint]()
  for(item <- rr) {
    ss.append(collectPoint(item.lng, item.lat, item.level, item.earliestTime, item.latestTime))
  }
  (ssid, ss.toArray)
  //val rd = res.toArray.sortWith(_.time > _.time)
  //(ssid, res.toArray.sortWith(_.time > _.time).slice(0, 86400))
}


def mergeTotalAndAccStr(total: String, acc:(String, Long, Array[gatherPoint])): String = {
  val tot = loadPoints(total)
  if (tot != None) {
    val wh = tot.get
    val meRes = mergeTotalAndAcc(wh._2, acc)
    noUpdateToStringFormat((wh._1, meRes))
  }
  else {
    ""
  }
}


def mergeTotalAndAccV(a: (String, Int, Array[collectPoint]),
                      b:(String, Int, Array[collectPoint])): (String, Int, Array[collectPoint]) = {
  var flagIndex: Set[Int] = Set()
  val res = ArrayBuffer[storePoint]()
  var ssid:String = ""

  if (a._2 == 1) {
    val didiLastestTime = a._3(0).latestTime(0)
    val baiduLastestTime = a._3(0).latestTime(1)
    val totalSsid = a._1
    val totalLastest = if (didiLastestTime > baiduLastestTime) didiLastestTime else baiduLastestTime
    val ele = b._1.split(",")
    val len = ele.length
    val newLastestTime = ele(len - 1).toLong
    val newSsid = ele.slice(0, len - 1).mkString(",")
    ssid = if (totalLastest > newLastestTime) totalSsid else newSsid

    for (item <- b._3) {
      val index = findPointV(item, a._3)
      if (index != -1) {
        res.append(addTotalAndAccV(item, a._3(index)))
        flagIndex += index
      } else {
        res.append(initAccPointV(item))
      }
    }
    val tt = a._3
    for(i <- 0 until a._3.length) {
      if(!flagIndex.contains(i)) {
        val time = if(tt(i).latestTime(0) > tt(i).latestTime(1)) tt(i).latestTime(0) else tt(i).latestTime(1)
        res.append(storePoint(tt(i).lng, tt(i).lat, tt(i).level, tt(i).earliestTime, tt(i).latestTime, time))
      }
    }
  } else { //b=total
    val didiLastestTime = b._3(0).latestTime(0)
    val baiduLastestTime = b._3(0).latestTime(1)
    val totalSsid = b._1
    val totalLastest = if (didiLastestTime > baiduLastestTime) didiLastestTime else baiduLastestTime
    val ele = a._1.split(",")
    val len = ele.length
    val newLastestTime = ele(len - 1).toLong
    val newSsid = ele.slice(0, len - 1).mkString(",")
    ssid = if (totalLastest > newLastestTime) totalSsid else newSsid

    for (item <- a._3) {
      val index = findPointV(item, b._3)
      if (index != -1) {
        res.append(addTotalAndAccV(item, b._3(index)))
        flagIndex += index
      } else {
        res.append(initAccPointV(item))
      }
    }
    val tt = b._3
    for(i <- 0 until b._3.length) {
      if(!flagIndex.contains(i)) {
        val time = if(tt(i).latestTime(0) > tt(i).latestTime(1)) tt(i).latestTime(0) else tt(i).latestTime(1)
        res.append(storePoint(tt(i).lng, tt(i).lat, tt(i).level, tt(i).earliestTime, tt(i).latestTime, time))
      }
    }
  }

  val rr = res.toArray.sortWith(_.time > _.time).slice(0, collectPointNum).array
  val ss = ArrayBuffer[collectPoint]()
  for(item <- rr) {
    ss.append(collectPoint(item.lng, item.lat, item.level, item.earliestTime, item.latestTime))
  }
  (ssid, 3, ss.toArray)
}

def genCollectPointFromAcc(acc:(String, Long, Array[gatherPoint])): (String, Array[collectPoint]) = {
  val ssid = acc._1
  val res = ArrayBuffer[storePoint]()
  for(item <- acc._3) {
    var lastestTime = new Array[Long](2)
    lastestTime(0) = item.didiTime
    lastestTime(1) = item.baiduTime
    var earlestTime = new Array[Long](2)
    earlestTime(0) = item.didiEarly
    earlestTime(1) = item.baiduEarly
    val time = if(item.didiTime > item.baiduTime) item.didiTime else item.baiduTime
    res.append(storePoint(item.lng, item.lat, item.level, earlestTime, lastestTime, time))
  }
  val rr = res.toArray.sortWith(_.time > _.time).slice(0, collectPointNum)
  val ss = ArrayBuffer[collectPoint]()
  for(item <- rr) {
    ss.append(collectPoint(item.lng, item.lat, item.level, item.earliestTime, item.latestTime))
  }
  (ssid, ss.toArray)
}


def noUpdateToStringFormat(d: (String, (String, Array[collectPoint]))): String = {
  val res = ArrayBuffer[String]()
  res.append(d._1)
  res.append(d._2._1)
  val flagA = ""
  val flagB = ""
  val flagC = ""
  for(item <- d._2._2) {
    res.append(item.lng.toString + flagC + item.lat.toString + flagB + item.level.slice(0, 7).mkString(flagC) + flagC
      + item.earliestTime(0).toString + flagC + item.latestTime(0).toString + flagB + item.level.slice(7, 14).mkString(flagC)
      + flagC + item.earliestTime(1).toString + flagC + item.latestTime(1).toString)
  }
  res.toArray.mkString(flagA)
}

object test{
  val now = new DateTime()
  val yesterday = now.minusDays(1)
  val now_y = now.getYear()
  val now_m = now.getMonthOfYear()
  val now_d = now.getDayOfMonth()
  val yesterday_y = yesterday.getYear()
  val yesterday_m = yesterday.getMonthOfYear()
  val yesterday_d = yesterday.getDayOfMonth()
  val begin = now.minusYears(1).getMillis()
  val end = now.getMillis()


  //val accOutputPath = args(4)
  val partitionNum = 15000
  val savaFileNum = 1000
  val now_etl_dir= "/user/map_loc/locsdk/etl-lzo-3/wifi/"+now_y+"/"+now_m+"/"+now_d+"/"
  val yesterday_total_dir= "/user/map_loc/wifi_location/ap_info_total/"+yesterday_y+"/"+yesterday_m+"/"+yesterday_d+"/"
  val now_total_dir= "/user/map_loc/wifi_location/ap_info_total/"+now_y+"/"+now_m+"/"+now_d+"/"
  val etl_sh = "hadoop fs -test -d " + now_etl_dir
  val total_sh = "hadoop fs -test -d " + yesterday_total_dir
  val etl_dir_exist = etl_sh !
  val total_dir_exist = total_sh !
  val all_dir_exist = false
  if(etl_dir_exist==0 && total_dir_exist==0){
    val all_dir_exist = true
  }

  if(all_dir_exist){
    val didiRdd = loadEtlSourceData(sc, now_etl_dir, partitionNum, 2.0, 1, begin, end)
  }




}
