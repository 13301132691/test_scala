package spark_test.Doc_Test.feature

/**
  * Created by BFD-725 on 2017/9/5.
  */
object ngram extends App{
  val spark = Spark.builder.master("local").appName("tokenizer").getOrCreate()
  val wordDataFrame = spark.createDataFrame(Seq(
    (0, Array("Hi", "I", "heard", "about", "Spark")),
    (1, Array("I", "wish", "Java", "could", "use", "case", "classes")),
    (2, Array("Logistic", "regression", "models", "are", "neat"))
  )).toDF("id", "words")

  val ngram = new NG().setN(3).setInputCol("words").setOutputCol("ngrams")
  val ndf = ngram.transform(wordDataFrame)
  ndf.show(false)
}

