package spark_test.Doc_Test.feature


import org.apache.spark.sql.functions._
/**
  * Created by BFD-725 on 2017/9/5.
  */
object tokenizer extends App{
  val spark = Spark.builder.master("local").appName("tokenizer").getOrCreate()
  val sentenceDataFrame = spark.createDataFrame(Seq(
    (0, "Hi I heard about Spark"),
    (1, "I wish Java could use case classes"),
    (2, "Logistic,regression,models,are,neat")
  )).toDF("id", "sentence")

  val tokenizer = new Tokenizer().setInputCol("sentence").setOutputCol("words")
  val regexTokenizer = new RTokenizer().setInputCol("sentence").setOutputCol("words").setPattern("\\W")
  val countTokens = udf {(words:Seq[String]) => words.length}
  val tokenized = tokenizer.transform(sentenceDataFrame)
  tokenized.select("sentence", "words")
    .withColumn("tokens", countTokens(col("words"))).show(false)

  val regexTokenized = regexTokenizer.transform(sentenceDataFrame)
  regexTokenized.select("sentence", "words")
    .withColumn("tokens", countTokens(col("words"))).show(false)
}
