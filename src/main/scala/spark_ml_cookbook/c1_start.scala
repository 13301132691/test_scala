package spark_ml_cookbook

import org.apache.spark.sql.SparkSession
import org.apache.log4j.Logger
import org.apache.log4j.Level

object c1_start {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val spark = SparkSession.builder().master("local[*]").appName("test").config("spark.sql.warehouse.dir",".").getOrCreate()
    val x = Array(1.0,5.0,8.0,10.0,15.0,21.0,27.0,30.0,38.0,45.0,50.0,64.0)
    val y = Array(5.0,1.0,4.0,11.0,25.0,18.0,33.0,20.0,30.0,43.0,55.0,57.0)

    val xRDD = spark.sparkContext.parallelize(x)
    val yRDD = spark.sparkContext.parallelize(y)
    val zipedRDD = xRDD.zip(yRDD)
    zipedRDD.collect().foreach(println)
  }
}
