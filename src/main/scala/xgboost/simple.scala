package xgboost

import java.util.Calendar
import org.apache.log4j.{Level,Logger}
import ml.dmlc.xgboost4j.scala.spark.XGBoost
import org.apache.spark.ml.feature._
import org.apache.spark.sql._
import org.apache.spark.sql.functions.lit

object simple {
  Logger.getLogger("org").setLevel(Level.WARN)

  def main(args: Array[String]): Unit = {
    val inputPath = "./data/xgboost/"

    val spark = SparkSession.builder().appName("simpleXGboost").master("local[*]").getOrCreate()
    val train = spark.read.format("libsvm").load("agaricus.txt.train")
    val test = spark.read.format("libsvm").load("agaricus.txt.test")
    val df = train.na.fill(0).sample(true,0.7,10)

  }
}
