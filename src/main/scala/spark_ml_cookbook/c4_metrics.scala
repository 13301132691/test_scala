package spark_ml_cookbook
import org.apache.spark.mllib.evaluation.RegressionMetrics
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.tree.DecisionTree
import org.apache.spark.sql.SparkSession
object c4_metrics {
  def main(args: Array[String]): Unit = {


    val spark = SparkSession
      .builder
      .master("local[*]")
      .appName("myRegressionMetrics")
      .config("spark.sql.warehouse.dir", ".")
      .getOrCreate()

    val rawData = spark.sparkContext.textFile("./data/spark-ml-cookbook/Chapter04/Data/breast-cancer-wisconsin.data")
    val data = rawData.map(_.trim)
      .filter(text => !(text.isEmpty || text.indexOf("?") > -1))
      .map { line =>
        val values = line.split(',').map(_.toDouble)
        val slicedValues = values.slice(1, values.size)
        val featureVector = Vectors.dense(slicedValues.init)
        val label = values.last / 2 - 1
        LabeledPoint(label, featureVector)

      }

    val splits = data.randomSplit(Array(0.7,0.3))
    val (trainingData, testData) = (splits(0), splits(1))

    val categoricalFeaturesInfo = Map[Int, Int]()
    val impurity = "variance"
    val maxDepth = 5
    val maxBins = 32

    val model = DecisionTree.trainRegressor(trainingData,categoricalFeaturesInfo,impurity,maxDepth,maxBins)
    val predictionsAndLabels = testData.map(example =>
      (model.predict(example.features), example.label)
    )
    val metrics = new RegressionMetrics(predictionsAndLabels)
    // Squared error
//    println(s"MSE = ${metrics.meanSquaredError}")
//    println(s"RMSE = ${metrics.rootMeanSquaredError}")
//
//    // R-squared
//    println(s"R-squared = ${metrics.r2}")

    // Mean absolute error
    println(s"MAE = ${metrics.meanAbsoluteError}")

    // Explained variance
    println(s"Explained variance = ${metrics.explainedVariance}")
    // $example off$
    println(predictionsAndLabels.filter(s=>s._1==s._2).count)
    println(predictionsAndLabels.filter(s=>s._1==s._2).count * 1.0 / predictionsAndLabels.count)
    spark.stop()
  }
}
