package spark_test.Doc_Test.feature

/**
  * Created by BFD-725 on 2017/9/5.
  */
object PCA extends App{
  val spark = Spark.builder.master("local").appName("tokenizer").getOrCreate()
  val data = Array(
    Vecs.sparse(5, Seq((1, 1.0), (3, 7.0))),
    Vecs.dense(2.0, 0.0, 3.0, 4.0, 5.0),
    Vecs.dense(4.0, 0.0, 0.0, 6.0, 7.0)
  )
  val df = spark.createDataFrame(data.map(Tuple1.apply)).toDF("features")
  val pca = new PCA().setInputCol("features").setOutputCol("pcaFeatures").setK(1).fit(df)
  val result = pca.transform(df)
  result.show(false)
}
