package spark_test.Doc_Test.feature

object vectorindexer extends App{
  val spark = Spark.builder.master("local").appName("vectorindexer").getOrCreate()
  val data = spark.read.format("libsvm").load("data/mllib/sample_libsvm_data.txt")
  val indexer = new VI().setInputCol("features").setOutputCol("indexed").setMaxCategories(10)
  val indexermodel = indexer.fit(data)
  val categoricalFeatures: Set[Int] = indexermodel.categoryMaps.keys.toSet
  println(s"Chose ${categoricalFeatures.size} categorical features: " +
    categoricalFeatures.mkString(", "))
  val indexData = indexermodel.transform(data)
  indexData.show(false)
}
