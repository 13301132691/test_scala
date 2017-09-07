package spark_test.Doc_Test.feature

/*
*不明白是如何划分bin的
+---+----+------+
| id|hour|result|
+---+----+------+
|  0|18.0|   2.0|
|  1|19.0|   2.0|
|  2| 8.0|   1.0|
|  3| 5.0|   1.0|
|  4| 2.2|   0.0|
+---+----+------+
*
* */

object QuantileDiscretizer extends App{
  val spark = Spark.builder.master("local").appName("buketizer").getOrCreate()
  val data = Array((0, 18.0), (1, 19.0), (2, 8.0), (3, 5.0), (4, 2.2))
  val df = spark.createDataFrame(data).toDF("id", "hour")
  val discretizer = new QD().setInputCol("hour").setOutputCol("result").setNumBuckets(4)
  val result = discretizer.fit(df).transform(df)
  result.show()
}
