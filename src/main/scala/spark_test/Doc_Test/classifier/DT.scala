package spark_test.Doc_Test.classifier

object DT extends App{
  val spark = Spark.builder.master("local").appName("DT").getOrCreate()
  val data = spark.read.format("libsvm").load("data/mllib/sample_libsvm_data.txt")
  val labelIndexer = new SI().setInputCol("label").setOutputCol("indexedlabel").fit(data)
  //不懂VectorIndexer的具体作用是什么？
  val featureIndexer = new VI().setInputCol("features").setOutputCol("indexedFeatures").setMaxCategories(10).fit(data)
//  (featureIndexer.transform(data).select("features","indexedFeatures").take(1))(0)(1)
  val Array(train,test) = data.randomSplit(Array(0.7,0.3))
  val dt = new DTC().setLabelCol("indexedlabel").setFeaturesCol("indexedFeatures")
  val labelConverter = new ITS().setInputCol("prediction").setOutputCol("predictedLabel").setLabels(labelIndexer.labels)
  val pipeline = new Pipeline().setStages(Array(labelIndexer,featureIndexer,dt,labelConverter))
  val model = pipeline.fit(train)
  val pre = model.transform(test)
  pre.select("predictedLabel", "label", "features").show(20,false)

  // Select (prediction, true label) and compute test error
  val eval = new MCE().setLabelCol("indexedlabel").setPredictionCol("prediction").setMetricName("accuracy")
  val acc = eval.evaluate(pre)
  println("Test Error = " + (1.0 - acc))
//Test Error = 0.09090909090909094

  val treeModel = model.stages(2).asInstanceOf[DTCM]
  println("Learned classification tree model:\n" + treeModel.toDebugString)
/*
*
* Learned classification tree model:
DecisionTreeClassificationModel (uid=dtc_d4bc2e35902c) of depth 2 with 5 nodes
  If (feature 351 <= 15.0)
   If (feature 183 <= 0.0)
    Predict: 0.0
   Else (feature 183 > 0.0)
    Predict: 1.0
  Else (feature 351 > 15.0)
   Predict: 0.0
* */
}
