package spark_test.Doc_Test.feature

/*
*
*
+---+------------------+-------+--------------+
| id|          features|clicked|     selectedF|
+---+------------------+-------+--------------+
|  7|[0.0,0.0,18.0,1.0]|    1.0|[0.0,18.0,1.0]|
|  8|[0.0,1.0,12.0,0.0]|    0.0|[0.0,12.0,0.0]|
|  9|[1.0,0.0,15.0,0.1]|    0.0|[1.0,15.0,0.1]|
+---+------------------+-------+--------------+
*
*
*/

object ChiSqSelector extends App{
  val spark = Spark.builder.master("local").appName("ChiSqSelector").getOrCreate()
  val data = Seq(
    (7, Vecs.dense(0.0, 0.0, 18.0, 1.0), 1.0),
    (8, Vecs.dense(0.0, 1.0, 12.0, 0.0), 0.0),
    (9, Vecs.dense(1.0, 0.0, 15.0, 0.1), 0.0)
  )

  val df = spark.createDataFrame(data).toDF("id", "features", "clicked")
  val selector = new CS().setNumTopFeatures(3).setFeaturesCol("features").setLabelCol("clicked").setOutputCol("selectedF")
  val res = selector.fit(df).transform(df)
  res.show()
}
