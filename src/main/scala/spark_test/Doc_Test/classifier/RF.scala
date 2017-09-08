package spark_test.Doc_Test.classifier

object RF extends App{
  val spark = Spark.builder.master("local").appName("RF").getOrCreate()
  val data = spark.read.format("libsvm").load("data/mllib/sample_libsvm_data.txt")
  val labelIndexer = new SI().setInputCol("label").setOutputCol("indexedlabel").fit(data)
  //不懂VectorIndexer的具体作用是什么？
  val featureIndexer = new VI().setInputCol("features").setOutputCol("indexedFeatures").setMaxCategories(10).fit(data)
  //  (featureIndexer.transform(data).select("features","indexedFeatures").take(1))(0)(1)
  val Array(train,test) = data.randomSplit(Array(0.7,0.3))
  val rf = new RFC().setLabelCol("indexedlabel").setFeaturesCol("indexedFeatures").setNumTrees(10)
  val labelConverter = new ITS().setInputCol("prediction").setOutputCol("predictedLabel").setLabels(labelIndexer.labels)
  val pipeline = new Pipeline().setStages(Array(labelIndexer,featureIndexer,rf,labelConverter))
  val model = pipeline.fit(train)
  val pre = model.transform(test)
  pre.select("predictedLabel", "label", "features").show(20,false)

  // Select (prediction, true label) and compute test error
  val eval = new MCE().setLabelCol("indexedlabel").setPredictionCol("prediction").setMetricName("accuracy")
  val acc = eval.evaluate(pre)
  println("Test Error = " + (1.0 - acc))

  val treeModel = model.stages(2).asInstanceOf[RFCM]
  println("Learned classification tree model:\n" + treeModel.toDebugString)
  /*
  *
  * Test Error = 0.038461538461538436
Learned classification tree model:
RandomForestClassificationModel (uid=rfc_b4394afd3fbb) with 10 trees
  Tree 0 (weight 1.0):
    If (feature 552 <= 0.0)
     If (feature 497 <= 0.0)
      Predict: 0.0
     Else (feature 497 > 0.0)
      Predict: 1.0
    Else (feature 552 > 0.0)
     If (feature 603 <= 0.0)
      Predict: 0.0
     Else (feature 603 > 0.0)
      Predict: 1.0
  Tree 1 (weight 1.0):
    If (feature 567 <= 0.0)
     If (feature 356 <= 0.0)
      Predict: 0.0
     Else (feature 356 > 0.0)
      Predict: 1.0
    Else (feature 567 > 0.0)
     If (feature 628 <= 0.0)
      Predict: 0.0
     Else (feature 628 > 0.0)
      Predict: 1.0
  Tree 2 (weight 1.0):
    If (feature 540 <= 65.0)
     If (feature 553 <= 0.0)
      Predict: 0.0
     Else (feature 553 > 0.0)
      Predict: 1.0
    Else (feature 540 > 65.0)
     Predict: 1.0
  Tree 3 (weight 1.0):
    If (feature 518 <= 16.0)
     If (feature 404 <= 25.0)
      Predict: 1.0
     Else (feature 404 > 25.0)
      Predict: 0.0
    Else (feature 518 > 16.0)
     Predict: 0.0
  Tree 4 (weight 1.0):
    If (feature 429 <= 0.0)
     If (feature 407 <= 0.0)
      Predict: 1.0
     Else (feature 407 > 0.0)
      Predict: 0.0
    Else (feature 429 > 0.0)
     Predict: 1.0
  Tree 5 (weight 1.0):
    If (feature 462 <= 0.0)
     Predict: 1.0
    Else (feature 462 > 0.0)
     Predict: 0.0
  Tree 6 (weight 1.0):
    If (feature 512 <= 0.0)
     If (feature 413 <= 0.0)
      Predict: 0.0
     Else (feature 413 > 0.0)
      Predict: 1.0
    Else (feature 512 > 0.0)
     Predict: 1.0
  Tree 7 (weight 1.0):
    If (feature 512 <= 0.0)
     If (feature 288 <= 15.0)
      Predict: 0.0
     Else (feature 288 > 15.0)
      Predict: 1.0
    Else (feature 512 > 0.0)
     Predict: 1.0
  Tree 8 (weight 1.0):
    If (feature 462 <= 0.0)
     Predict: 1.0
    Else (feature 462 > 0.0)
     Predict: 0.0
  Tree 9 (weight 1.0):
    If (feature 301 <= 0.0)
     If (feature 578 <= 35.0)
      Predict: 0.0
     Else (feature 578 > 35.0)
      If (feature 550 <= 43.0)
       Predict: 0.0
      Else (feature 550 > 43.0)
       If (feature 183 <= 0.0)
        Predict: 0.0
       Else (feature 183 > 0.0)
        Predict: 1.0
    Else (feature 301 > 0.0)
     Predict: 1.0
  * */
}
