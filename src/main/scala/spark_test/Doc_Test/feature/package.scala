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
  type CV = org.apache.spark.ml.feature.CountVectorizer
  type CVM = org.apache.spark.ml.feature.CountVectorizerModel
  type RTokenizer =  org.apache.spark.ml.feature.RegexTokenizer
  type SWR =  org.apache.spark.ml.feature.StopWordsRemover
  type NG =  org.apache.spark.ml.feature.NGram
  type PCA =  org.apache.spark.ml.feature.PCA

  /*object*/
  val Vecs = org.apache.spark.ml.linalg.Vectors
  val Spark = org.apache.spark.sql.SparkSession
  val _Tokenizer = new Tokenizer()
  val Row = org.apache.spark.sql.Row
  val CVM = org.apache.spark.ml.feature.CountVectorizerModel
  val CV = org.apache.spark.ml.feature.CountVectorizer
}
