package spark_ml_cookbook
import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession
object c4_split {
  def main(args: Array[String]): Unit = {

    Logger.getLogger("org").setLevel(Level.ERROR)
    Logger.getLogger("akka").setLevel(Level.ERROR)

    // setup SparkSession to use for interactions with Spark
    val spark = SparkSession
      .builder
      .master("local[*]")
      .appName("Data Splitting")
      .config("spark.sql.warehouse.dir", ".")
      .getOrCreate()

    // http://archive.ics.uci.edu/ml/machine-learning-databases/00359/NewsAggregatorDataset.zip
    val data = spark.read.csv("./data/spark-ml-cookbook/Chapter04/Data/newsCorpora.csv")

    val rowCount = data.count()
    println("Original RowCount=" + rowCount)

    val splitData = data.randomSplit(Array(0.7, 0.2, 0.1))

    val trainingSet = splitData(0)
    val devSet = splitData(1)
    val testSet = splitData(2)
    val trainingSetCount = trainingSet.count()
    val devSetCount = devSet.count()
    val testSetCount = testSet.count()

    println("trainingSet RowCount=" + trainingSetCount)
    println("devSet RowCount=" + devSetCount)
    println("testSet RowCount=" + testSetCount)
    println("Combined RowCount=" + (trainingSetCount+devSetCount+testSetCount))

    spark.stop()
  }
}
