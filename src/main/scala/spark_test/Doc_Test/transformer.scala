package spark_test.Doc_Test

import org.apache.spark.ml.classification.LogisticRegression
import org.apache.spark.ml.linalg.{Vectors,Vector}
import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.ml.param.ParamMap

/**
  * Created by BFD-725 on 2017/9/4.
  */
object transformer1 extends App{
  val spark  = SparkSession.builder.master("local").appName("pipe").getOrCreate()
  import spark.implicits._
  val training = spark.createDataFrame(Seq(
    (1.0, Vectors.dense(0.0, 1.1, 0.1)),
    (0.0, Vectors.dense(2.0, 1.0, -1.0)),
    (0.0, Vectors.dense(2.0, 1.3, 1.0)),
    (1.0, Vectors.dense(0.0, 1.2, -0.5))
  )).toDF("label", "features")

  val lr = new LogisticRegression()
  println("LogisticRegression parameters:\n" + lr.explainParams()+ "\n")

  lr.setMaxIter(10).setRegParam(0.01)
  val model1 = lr.fit(training)
  model1.parent.extractParamMap()

  val paramMap = ParamMap(lr.maxIter -> 10).put(lr.regParam->0.02)
  val paramMap2 = ParamMap(lr.probabilityCol -> "myProbability")  // Change output column name.
  val paramMapCombined = paramMap ++ paramMap2
  val model2 = lr.fit(training, paramMapCombined)
  println("Model 2 was fit using parameters: " + model2.parent.extractParamMap)

  // Prepare test data.
  val test = spark.createDataFrame(Seq(
    (1.0, Vectors.dense(-1.0, 1.5, 1.3)),
    (0.0, Vectors.dense(3.0, 2.0, -0.1)),
    (1.0, Vectors.dense(0.0, 2.2, -1.5))
  )).toDF("label", "features")

model2.transform(test).select("features", "label", "myProbability", "prediction").collect().foreach{case Row(features: Vector, label: Double, prob: Vector, prediction: Double)=>println(s"($features, $label) -> prob=$prob, prediction=$prediction")}
}
