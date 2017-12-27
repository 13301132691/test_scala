import org.apache.spark.ml.feature._
import org.apache.spark.ml.linalg.{Vector,Vectors}
import org.apache.spark.ml.evaluation.RegressionEvaluator
import org.apache.spark.ml.regression.LinearRegression
import org.apache.spark.ml.tuning.{ParamGridBuilder,TrainValidationSplit}
val Array(training,test) = data.randomSplit(Array(0.7,0.3))
val lr = new LinearRegression().setMaxIter(10)
val paramGrid = new ParamGridBuilder().addGrid(lr.regParam,Array(0.1,0.01)).addGrid(lr.fitIntercept).addGrid(lr.elasticNetParam,Array(0.0,0.5,1.0)).build()





