import org.apache.spark
import org.apache.spark.ml.feature.Normalizer
import org.apache.spark.ml.linalg.Vectors
import org.apache.spark.sql.SparkSession
spark = (SparkSession.builder()
  .appName("test")
  .master("local[1]")
  .enableHiveSupport()
  .config("spark.yarn.dist.files","conf/hive-site.xml")
  .getOrCreate())
val dataFrame = spark.createDataFrame(Seq(
  (0, Vectors.dense(1.0, 0.5, -1.0)),
  (1, Vectors.dense(2.0, 1.0, 1.0)),
  (2, Vectors.dense(4.0, 10.0, 2.0))
)).toDF("id", "features")