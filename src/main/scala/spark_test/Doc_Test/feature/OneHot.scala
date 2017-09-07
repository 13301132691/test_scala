package spark_test.Doc_Test.feature

object OneHot {
  import org.apache.spark.ml.feature.{OneHotEncoder, StringIndexer}
  val spark = Spark.builder.master("local").appName("onehot").getOrCreate()
  val df = spark.createDataFrame(Seq(
    (0, "a"),
    (1, "b"),
    (2, "c"),
    (3, "a"),
    (4, "a"),
    (5, "c"),
    (6, "e")
  )).toDF("id", "category")

  val indexer = new StringIndexer().setInputCol("category").setOutputCol("categoryIndex").fit(df)
  val indexed = indexer.transform(df)

  val encoder = new OneHotEncoder().setInputCol("categoryIndex").setOutputCol("categoryVec")

  val encoded = encoder.transform(indexed)
  encoded.show()
}

/*
*
* vector为sparse类型
* 运行结果:
| id|category|categoryIndex|  categoryVec|
+---+--------+-------------+-------------+
|  0|       a|          0.0|(3,[0],[1.0])|
|  1|       b|          3.0|    (3,[],[])|
|  2|       c|          1.0|(3,[1],[1.0])|
|  3|       a|          0.0|(3,[0],[1.0])|
|  4|       a|          0.0|(3,[0],[1.0])|
|  5|       c|          1.0|(3,[1],[1.0])|
|  6|       e|          2.0|(3,[2],[1.0])|
+---+--------+-------------+-------------+
* */
