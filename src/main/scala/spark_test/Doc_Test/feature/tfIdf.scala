package spark_test.Doc_Test.feature

/**
  * Created by BFD-725 on 2017/9/4.
  */
object tfIdf extends App{
  val spark = Spark.builder.master("local").appName("tfidf").getOrCreate()
  val sentenceData = spark.createDataFrame(Seq(
    (0.0, "Hi I heard about Spark"),
    (0.0, "I wish Java could use case classes"),
    (1.0, "Logistic regression models are neat")
  )).toDF("label", "sentence")

  val tokenizer = new Tokenizer().setInputCol("sentence").setOutputCol("words")
  val wordsData = tokenizer.transform(sentenceData)

  val hashingTF = new HTF().setInputCol("words").setOutputCol("rawFeatures").setNumFeatures(5)

  val featurizedData = hashingTF.transform(wordsData)
  // alternatively, CountVectorizer can also be used to get term frequency vectors

  val idf = new IDF().setInputCol("rawFeatures").setOutputCol("features")
  val idfModel = idf.fit(featurizedData)

  val rescaledData = idfModel.transform(featurizedData)
  val res = rescaledData.select("label", "features")
}
