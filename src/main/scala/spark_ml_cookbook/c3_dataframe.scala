package spark_ml_cookbook
import org.apache.spark.sql._
import org.apache.log4j.Logger
import org.apache.log4j.Level
object c3_dataframe {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    Logger.getLogger("akka").setLevel(Level.ERROR)

    val spark = SparkSession
      .builder
      .master("local[*]")
      .appName("myDataFrame")
      .config("spark.sql.warehouse.dir", ".")
      .getOrCreate()

    import spark.implicits._
    val myseq = Seq( ("Sammy","North",113,46.0),("Sumi","South",110,41.0), ("Sunny","East",111,51.0),("Safron","West",113,2.0 ))
    val df1 = spark.createDataFrame(myseq).toDF("Name","Region","dept","Hours")
    df1.show()
    df1.printSchema
    df1
  }
}
