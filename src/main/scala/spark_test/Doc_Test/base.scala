//package spark_test.Doc_Test
//import feature._
///**
//  * Created by BFD-725 on 2017/9/4.
//  */
//class base extends App{
//  import org.apache.spark.ml.Pipeline
//  import org.apache.spark.sql.{Row, SparkSession}
//  import org.apache.spark.ml.classification.LogisticRegression
//  import org.apache.spark.ml.feature.{HashingTF, Tokenizer}
//  import org.apache.spark.ml.linalg.Vector
//  import org.apache.spark.sql.Row
//  import org.apache.spark.ml.classification.LogisticRegression
//  import org.apache.spark.ml.linalg.{Vectors,Vector}
//  import org.apache.spark.sql.{Row, SparkSession}
//  import org.apache.spark.ml.param.ParamMap
//  import org.apache.spark.ml.feature.{HashingTF, IDF, Tokenizer}
//  val spark  = SparkSession.builder.master("local").appName("pipe").getOrCreate()
//  import spark.implicits._
//  val tokenizer = new Tokenizer()
//  val hashingTF = new HashingTF()
//  val idf = new IDF()
//  val pipeline = new PipeLine
//}
