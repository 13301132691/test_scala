package spark_test.Doc_Test

package object classifier {
  type Pipeline =  org.apache.spark.ml.Pipeline
  type RF =  org.apache.spark.ml.feature.RFormula
  type CS =  org.apache.spark.ml.feature.ChiSqSelector
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
  type VI =  org.apache.spark.ml.feature.VectorIndexer
  type NL = org.apache.spark.ml.feature.Normalizer
  type BK = org.apache.spark.ml.feature.Bucketizer
  type QD = org.apache.spark.ml.feature.QuantileDiscretizer
  type BRPLSH = org.apache.spark.ml.feature.BucketedRandomProjectionLSH
  type MH = org.apache.spark.ml.feature.MinHashLSH

  /*object*/
  val Vecs = org.apache.spark.ml.linalg.Vectors
  val Spark = org.apache.spark.sql.SparkSession
  val _Tokenizer = new Tokenizer()
  val Row = org.apache.spark.sql.Row
  val CVM = org.apache.spark.ml.feature.CountVectorizerModel
  val CV = org.apache.spark.ml.feature.CountVectorizer
  val f = org.apache.spark.sql.functions
}
