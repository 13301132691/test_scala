package spark_test.Doc_Test


import org.apache.spark.ml.Pipeline
import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.ml.classification.LogisticRegression
import org.apache.spark.ml.feature.{HashingTF, Tokenizer}
import org.apache.spark.ml.linalg.Vector
import org.apache.spark.sql.Row

/**
  * Created by BFD-725 on 2017/9/4.
  */
object transformer extends App{
  val spark  = SparkSession.builder.master("local").appName("pipe").getOrCreate()
  import spark.implicits._
  val training = spark.createDataFrame(Seq(
    (0L, "a b c d e spark", 1.0),
    (1L, "b d", 0.0),
    (2L, "spark f g h", 1.0),
    (3L, "hadoop mapreduce", 0.0)
  )).toDF("id", "text", "label")

  // Configure an ML pipeline, which consists of three stages: tokenizer, hashingTF, and lr.
  val tokenizer = new Tokenizer()
    .setInputCol("text")
    .setOutputCol("words")
  val hashingTF = new HashingTF()
    .setNumFeatures(1000)
    .setInputCol(tokenizer.getOutputCol)
    .setOutputCol("features")
  val lr = new LogisticRegression()
    .setMaxIter(10)
    .setRegParam(0.001)
  val pipeline = new Pipeline().setStages(Array(tokenizer,hashingTF,lr))
  val model = pipeline.fit(training)
  model.write.overwrite().save("tmp/spark-logistic-regression-model1")
  pipeline.write.overwrite().save("tmp/unfit-lr-model1")
  val test = spark.createDataFrame(Seq(
    (4L, "spark i j k"),
    (5L, "l m n"),
    (6L, "spark hadoop spark"),
    (7L, "apache hadoop")
  )).toDF("id", "text")

  // Make predictions on test documents.
  model.transform(test).select("id", "text", "probability", "prediction").collect().foreach { case Row(id: Long, text: String, prob: Vector, prediction: Double) =>
      println(s"($id, $text) --> prob=$prob, prediction=$prediction")
    }
}