package spark_test.Doc_Test.feature

/**
  * Created by BFD-725 on 2017/9/5.
  */
object countvectorizer extends App{
  val spark = Spark.builder.master("local").appName("countvectorizer").getOrCreate()
  val df = spark.createDataFrame(Seq(
    (0, Array("a", "b", "c")),
    (1, Array("a", "b", "b", "c", "a"))
  )).toDF("id", "words")
  val cvModel: CVM = new CV().setInputCol("words").setOutputCol("features").setVocabSize(3).setMinDF(2).fit(df)
  val res = cvModel.transform(df)
  val cvm = new CVM(Array("a","b","c"))
  res.take(2)
}
