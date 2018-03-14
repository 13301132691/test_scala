package xgboost


import ml.dmlc.xgboost4j.scala.DMatrix
import ml.dmlc.xgboost4j.scala.XGBoost

import java.io.File
import java.io.PrintWriter
object local {

  def saveDumpModel(modelPath: String, modelInfos: Array[String]): Unit = {
    val writer = new PrintWriter(modelPath, "UTF-8")
    for (i <- 0 until modelInfos.length) {
      writer.print(s"booster[$i]:\n")
      writer.print(modelInfos(i))
    }
    writer.close()
  }

  def checkPredicts(fPredicts: Array[Array[Float]], sPredicts: Array[Array[Float]]): Boolean = {
    require(fPredicts.length == sPredicts.length, "the comparing predicts must be with the same " +
      "length")
    for (i <- fPredicts.indices) {
      if (!java.util.Arrays.equals(fPredicts(i), sPredicts(i))) {
        return false
      }
    }
    true
  }
  def main(args: Array[String]): Unit = {
    val trainData = new DMatrix("./data/xgboost/agaricus.txt.train")
    val testData = new DMatrix("./data/xgboost/agaricus.txt.test")
    val paramMap = List(
      "eta" -> 0.1,
      "max_depth" -> 5,
      "objective" -> "binary:logistic"
    ).toMap

    val watches = List(
      "train" -> trainData,
      "test" -> testData
    ).toMap
    val round = 2

    val model = XGBoost.train(trainData,paramMap,round,watches)
    val pred = model.predict(testData)
    print(pred.map(_.toList).toList)

    val file = new File("./model/xgboost/")
    if (!file.exists()) {
      file.mkdirs()
    }
    model.saveModel(file.getAbsolutePath + "/localModel")
    val modelInfos = model.getModelDump("./data/xgboost/featmap.txt",false)
    saveDumpModel(file.getAbsolutePath + "/dump.raw.txt", modelInfos)
    testData.saveBinary(file.getAbsolutePath + "/dtest.buffer")

    val model2 = XGBoost.loadModel(file.getAbsolutePath + "/localModel")
    val testData2 = new DMatrix(file.getAbsolutePath + "/dtest.buffer")
    val predicts2 = model2.predict(testData2)
    println(checkPredicts(pred, predicts2).toString+"end")

  }
}
