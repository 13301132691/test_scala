import org.apache.spark.sql._
import org.apache.spark.ml.linalg._
val d = Matrices.dense(3,3,Array(23.0, 11.0, 17.0, 34.3, 33.0, 24.5, 21.3,22.6,22.2))
val sparseMat33= Matrices.sparse(3,3 ,Array(0, 2, 3, 6) ,Array(0, 2, 1, 0, 1, 2),Array(1.0, 2.0, 3.0, 4.0, 5.0, 6.0)).toSparse
sparseMat33.colPtrs
val v1 = Vectors.dense(1,2,3)
sparseMat33.multiply(sparseMat33.toDense.transpose)
sparseMat33
val spark = SparkSession
  .builder
  .master("local[*]")
  .appName("myDataFrame")
  .config("spark.sql.warehouse.dir", ".")
  .getOrCreate()
import spark.implicits._
val myseq = Seq( ("Sammy","North",113,46.0),("Sumi","South",110,41.0), ("Sunny","East",111,51.0),("Safron","West",113,2.0 ))
spark.createDataFrame(myseq).toDF
val productsRDD = spark.sparkContext.textFile("../data/sparkml2/chapter3/products13.txt") //Product file
val prodRDD = productsRDD.map {
  line => val cols = line.trim.split(",")
    (cols(0).toInt, cols(1), cols(2), cols(3).toDouble)
}
spark.read