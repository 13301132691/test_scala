package spark_test.Doc_Test.feature

/**
  * Created by BFD-725 on 2017/9/5.
  */
object stopword extends App{
  val spark = Spark.builder.master("local").appName("tokenizer").getOrCreate()
  val remover = new SWR().setInputCol("raw").setOutputCol("filtered")

  val dataSet = spark.createDataFrame(Seq(
    (0, Seq("I", "saw", "the", "red", "balloon")),
    (1, Seq("Mary", "had", "a", "little", "lamb"))
  )).toDF("id", "raw")
  remover.transform(dataSet).show(false)
}
