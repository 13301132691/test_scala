package spark_test.Doc_Test.feature
/*
*
*
* +--------+----------------+
|features|bucketedFeatures|
+--------+----------------+
|  -999.9|             0.0|
|    -0.5|             1.0|
|    -0.3|             1.0|
|     0.0|             2.0|
|     0.2|             2.0|
|   999.9|             3.0|
+--------+----------------+
*
*
*
*
*
* */

object buketizer extends App{

  val splits = Array(Double.NegativeInfinity, -0.5, 0.0, 0.5, Double.PositiveInfinity)
  val spark = Spark.builder.master("local").appName("buketizer").getOrCreate()
  val data = Array(-999.9, -0.5, -0.3, 0.0, 0.2, 999.9)
  val dataFrame = spark.createDataFrame(data.map(Tuple1.apply)).toDF("features")

  val bucketizer = new BK().setInputCol("features").setOutputCol("bucketedFeatures").setSplits(splits)

  // Transform original data into its bucket index.
  val bucketedData = bucketizer.transform(dataFrame)

  println(s"Bucketizer output with ${bucketizer.getSplits.length-1} buckets")
  bucketedData.show()

}
