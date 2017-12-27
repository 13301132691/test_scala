import org.apache.spark.sql.SparkSession
val spark = SparkSession.builder().master("local[3]").appName("test").getOrCreate()
val sc = spark.sparkContext
import spark.implicits._
case class n(num:Int)
var arr = sc.parallelize(Array(1,2,3,4,5))//.toDF()
val result1 = arr.flatMap(x=>List(x+1)).collect()
val result2 = arr.map(x=>List(x+1)).collect()
val res3 = spark.read.format("json").option("header","true").csv("")
res3.write.format("json").option("header","true")