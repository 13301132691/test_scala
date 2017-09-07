package spark_test.Doc_Test.feature
//不懂vectorslicer要干啥
object vectorslicer extends App{
  val spark = Spark.builder.master("local").appName("vectorslicer").getOrCreate()
  val data = Array(Vecs.sparse(3, Seq((0, -2.0), (1, 2.3))),Vecs.dense(-2.0, 2.3, 0.0))
  val df = spark.createDataFrame(data.map(Tuple1.apply)).toDF("")
}
