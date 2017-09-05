package spark_test.Doc_Test.feature

/**
  * Created by BFD-725 on 2017/9/5.
  */
object word2vec extends App{
  val spark = Spark.builder.master("local").appName("word2vec").getOrCreate()
  val documentDF = spark.createDataFrame(Seq(
    "Hi I heard about Spark".split(" "),
    "I wish Java could use case classes".split(" "),
    "Logistic regression models are neat".split(" ")
  ).map(Tuple1.apply)).toDF("text")

  // Learn a mapping from words to Vectors.
  val word2Vec = new W2V().setInputCol("text").setOutputCol("result").setVectorSize(20).setMinCount(0)
  val model = word2Vec.fit(documentDF)
  val res = model.transform(documentDF)
  res.collect().foreach {
    case Row(text:Seq[_],features:Vec) => println(s"Text: [${text.mkString(", ")}] => \nVector: $features\n")
  }
}
