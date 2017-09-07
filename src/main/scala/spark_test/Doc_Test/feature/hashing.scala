package spark_test.Doc_Test.feature



object hashing extends App{
  val spark = Spark.builder.master("local").appName("hashing").getOrCreate()

  val dfA = spark.createDataFrame(Seq(
    (0, Vecs.sparse(6, Seq((0, 1.0), (1, 1.0), (2, 1.0)))),
    (1, Vecs.sparse(6, Seq((2, 1.0), (3, 1.0), (4, 1.0)))),
    (2, Vecs.sparse(6, Seq((0, 1.0), (2, 1.0), (4, 1.0))))
  )).toDF("id", "features")

  val dfB = spark.createDataFrame(Seq(
    (3, Vecs.sparse(6, Seq((1, 1.0), (3, 1.0), (5, 1.0)))),
    (4, Vecs.sparse(6, Seq((2, 1.0), (3, 1.0), (5, 1.0)))),
    (5, Vecs.sparse(6, Seq((1, 1.0), (2, 1.0), (4, 1.0))))
  )).toDF("id", "features")

  val key = Vecs.sparse(6, Seq((1, 1.0), (3, 1.0)))

  val brp = new BRPLSH().setBucketLength(4.0).setNumHashTables(6).setInputCol("features").setOutputCol("hashes")

  val model = brp.fit(dfA)

  // Feature Transformation
  println("The hashed dataset where hashed values are stored in the column 'hashes':")
  model.transform(dfA).show()

  // Compute the locality sensitive hashes for the input rows, then perform approximate
  // similarity join.
  // We could avoid computing hashes by passing in the already-transformed dataset, e.g.
  // `model.approxSimilarityJoin(transformedA, transformedB, 1.5)`
  println("Approximately joining dfA and dfB on Euclidean distance smaller than 1.5:")
  model.approxSimilarityJoin(dfA, dfB, 1.5, "EuclideanDistance").
    select(f.col("datasetA.id").alias("idA"), f.col("datasetB.id").alias("idB"), f.col("EuclideanDistance")).show()

  // Compute the locality sensitive hashes for the input rows, then perform approximate nearest
  // neighbor search.
  // We could avoid computing hashes by passing in the already-transformed dataset, e.g.
  // `model.approxNearestNeighbors(transformedA, key, 2)`
  println("Approximately searching dfA for 2 nearest neighbors of the key:")
  model.approxNearestNeighbors(dfA, key, 2).show(false)

/*
*
*
+---+-------------------------+----------------------------------------------+------------------+
|id |features                 |hashes                                        |distCol           |
+---+-------------------------+----------------------------------------------+------------------+
|0  |(6,[0,1,2],[1.0,1.0,1.0])|[[-1.0], [-1.0], [-1.0], [0.0], [0.0], [-1.0]]|1.7320508075688772|
|1  |(6,[2,3,4],[1.0,1.0,1.0])|[[0.0], [0.0], [0.0], [-1.0], [-1.0], [0.0]]  |1.7320508075688772|
+---+-------------------------+----------------------------------------------+------------------+
*
* */


  val mh = new MH().setNumHashTables(5).setInputCol("features").setOutputCol("hashes")

  val model2 = mh.fit(dfA)

  // Feature Transformation
  println("The hashed dataset where hashed values are stored in the column 'hashes':")
  model2.transform(dfA).show(false)

  // Compute the locality sensitive hashes for the input rows, then perform approximate
  // similarity join.
  // We could avoid computing hashes by passing in the already-transformed dataset, e.g.
  // `model.approxSimilarityJoin(transformedA, transformedB, 0.6)`
  println("Approximately joining dfA and dfB on Jaccard distance smaller than 0.6:")
  model2.approxSimilarityJoin(dfA, dfB, 0.6, "JaccardDistance")
    .select(f.col("datasetA.id").alias("idA"),
      f.col("datasetB.id").alias("idB"),
      f.col("JaccardDistance")).show()
/*
*
*
+---+---+---------------+
|idA|idB|JaccardDistance|
+---+---+---------------+
|  0|  5|            0.5|
|  1|  5|            0.5|
|  2|  5|            0.5|
|  1|  4|            0.5|
+---+---+---------------+
*
* */
  // Compute the locality sensitive hashes for the input rows, then perform approximate nearest
  // neighbor search.
  // We could avoid computing hashes by passing in the already-transformed dataset, e.g.
  // `model.approxNearestNeighbors(transformedA, key, 2)`
  // It may return less than 2 rows when not enough approximate near-neighbor candidates are
  // found.
  println("Approximately searching dfA for 2 nearest neighbors of the key:")
  model2.approxNearestNeighbors(dfA, key, 2).show(false)
/*
*
+---+-------------------------+--------------------------------------------------------------------------------------+-------+
|id |features                 |hashes                                                                                |distCol|
+---+-------------------------+--------------------------------------------------------------------------------------+-------+
|0  |(6,[0,1,2],[1.0,1.0,1.0])|[[-2.031299587E9], [-1.974869772E9], [-1.974047307E9], [4.95314097E8], [7.01119548E8]]|0.75   |
+---+-------------------------+--------------------------------------------------------------------------------------+-------+
*
* */
}
