package xgboost

import ml.dmlc.xgboost4j.scala.Booster
import ml.dmlc.xgboost4j.scala.spark.XGBoost
import org.apache.spark.sql.SparkSession

object sparkDF {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().appName("sparkDF").master("local[*]").getOrCreate()
    val train = spark.read.format("libsvm").load("./data/xgboost/agaricus.txt.train")
    val test = spark.read.format("libsvm").load("./data/xgboost/agaricus.txt.test")
    val paramMap = List(
      "eta" -> 0.1f,
      "max_depth" -> 2,
      "objective" -> "binary:logistic"
    ).toMap
    val xgboostModel = XGBoost.trainWithDataFrame(train,paramMap,3,2,useExternalMemory = true)
    val pred = xgboostModel.transform(test)
    pred.select("label","probabilities","prediction").show(20,false)


  }
}
