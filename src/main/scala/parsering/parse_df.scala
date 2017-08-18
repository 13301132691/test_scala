package parsering

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.FloatType

import scala.util.parsing.combinator.RegexParsers
import scala.util.parsing.input.CharSequenceReader

class parse_df {

}
object parse_df{
  val spark = SparkSession.builder().master("local").appName("parse_df").getOrCreate()
  val iris = spark.read.csv("data/iris.csv").toDF("c0","c1","c2","c3","c4")
  val iris1 = iris.withColumn("c3", iris.col("c3").cast(FloatType))
  val im = iris1.groupBy("c4").max("c3")

  "在iris数据集上，按label汇总，求f1的最大值"
  "iris数据集，按label汇总，求f1的最大值"


}

class MyParser extends RegexParsers{
  override val skipWhitespace = false
  var dfname$ = ""

  val zai = "在"
  val shujuji = "数据集"
  val dfname = "\\w+".r

  val mainParser = zai ~> dfname <~ shujuji ^^ {
    case name =>
      MyParsingResult(name,"")
  }

  mainParser(new CharSequenceReader("在iris数据集")) match {
    case Success(v,_) => v
    case _ => throw null
  }

}

case class MyParsingResult(dfname:String,groupBy:String)
