package spark_test.Doc_Test

/**
  * Created by BFD-725 on 2017/9/4.
  */
package object feature {
  /*class*/
  type Pipeline =  org.apache.spark.ml.Pipeline

  type LR = org.apache.spark.ml.classification.LogisticRegression
  type HTF = org.apache.spark.ml.feature.HashingTF
  type Tokenizer = org.apache.spark.ml.feature.Tokenizer
  type Vec = org.apache.spark.ml.linalg.Vector
  type  PM = org.apache.spark.ml.param.ParamMap
  type IDF = org.apache.spark.ml.feature.IDF
  type W2V = org.apache.spark.ml.feature.Word2Vec
  

  /*object*/
  val Vecs = org.apache.spark.ml.linalg.Vectors
  val Spark = org.apache.spark.sql.SparkSession
  val _Tokenizer = new Tokenizer()
  val Row = org.apache.spark.sql.Row
}
