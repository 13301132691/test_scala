package spark_test.Doc_Test.feature

object normalizer extends App{
  val spark = Spark.builder.master("local").appName("normalizer").getOrCreate()
  val dataFrame = spark.createDataFrame(Seq(
    (0, Vecs.dense(1.0, 0.5, -1.0)),
    (1, Vecs.dense(2.0, 1.0, 1.0)),
    (2, Vecs.dense(4.0, 10.0, 2.0))
  )).toDF("id", "features")

  // Normalize each Vector using $L^1$ norm.
  val normalizer = new NL().setInputCol("features").setOutputCol("normFeatures").setP(1.0)

  val l1NormData = normalizer.transform(dataFrame)
  println("Normalized using L^1 norm")
  l1NormData.show()

  /*
  *
  *
  * +---+--------------+------------------+
| id|      features|      normFeatures|
+---+--------------+------------------+
|  0|[1.0,0.5,-1.0]|    [0.4,0.2,-0.4]|
|  1| [2.0,1.0,1.0]|   [0.5,0.25,0.25]|
|  2|[4.0,10.0,2.0]|[0.25,0.625,0.125]|
+---+--------------+------------------+

  *
  *
  * */

  // Normalize each Vector using $L^\infty$ norm.
  val lInfNormData = normalizer.transform(dataFrame, normalizer.p -> Double.PositiveInfinity)
  println("Normalized using L^inf norm")
  lInfNormData.show()
  /*
  *
  *
  *
 +---+--------------+--------------+
| id|      features|  normFeatures|
+---+--------------+--------------+
|  0|[1.0,0.5,-1.0]|[1.0,0.5,-1.0]|
|  1| [2.0,1.0,1.0]| [1.0,0.5,0.5]|
|  2|[4.0,10.0,2.0]| [0.4,1.0,0.2]|
+---+--------------+--------------+
  *
  * */
}
