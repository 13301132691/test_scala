package spark_test.Doc_Test.feature

/*
* 将country one-hot编码，与hour assemble
* +---+-------+----+-------+--------------+-----+
| id|country|hour|clicked|      features|label|
+---+-------+----+-------+--------------+-----+
|  7|     US|  18|    1.0|[0.0,0.0,18.0]|  1.0|
|  8|     CA|  12|    0.0|[1.0,0.0,12.0]|  0.0|
|  9|     NZ|  15|    0.0|[0.0,1.0,15.0]|  0.0|
+---+-------+----+-------+--------------+-----+

*
* */

object Rformula extends App{
  val spark = Spark.builder.master("local").appName("tokenizer").getOrCreate()
  val dataset = spark.createDataFrame(Seq(
    (7, "US", 18, 1.0),
    (8, "CA", 12, 0.0),
    (9, "NZ", 15, 0.0)
  )).toDF("id", "country", "hour", "clicked")
  val formula = new RF().setFormula("clicked ~ country + hour").setFeaturesCol("features").setLabelCol("label")
  val output = formula.fit(dataset).transform(dataset)
  output.select("features", "label").show(false)
}
