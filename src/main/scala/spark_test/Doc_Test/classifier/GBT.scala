package spark_test.Doc_Test.classifier

object GBT extends App{
  val spark = Spark.builder.master("local").appName("GBT").getOrCreate()
  val data = spark.read.format("libsvm").load("data/mllib/sample_libsvm_data.txt")
  val labelIndexer = new SI().setInputCol("label").setOutputCol("indexedlabel").fit(data)
  //不懂VectorIndexer的具体作用是什么？
  val featureIndexer = new VI().setInputCol("features").setOutputCol("indexedFeatures").setMaxCategories(10).fit(data)
  //  (featureIndexer.transform(data).select("features","indexedFeatures").take(1))(0)(1)
  val Array(train,test) = data.randomSplit(Array(0.7,0.3))
  val gbt = new GBT().setLabelCol("indexedlabel").setFeaturesCol("indexedFeatures").setMaxIter(10)
  val labelConverter = new ITS().setInputCol("prediction").setOutputCol("predictedLabel").setLabels(labelIndexer.labels)
  val pipeline = new Pipeline().setStages(Array(labelIndexer,featureIndexer,gbt,labelConverter))
  val model = pipeline.fit(train)
  val pre = model.transform(test)
  pre.select("predictedLabel", "label", "features").show(20,false)

  // Select (prediction, true label) and compute test error
  val eval = new MCE().setLabelCol("indexedlabel").setPredictionCol("prediction").setMetricName("accuracy")
  val acc = eval.evaluate(pre)
  println("Test Error = " + (1.0 - acc))

  val treeModel = model.stages(2).asInstanceOf[GBTM]
  println("Learned classification tree model:\n" + treeModel.toDebugString)
  /*
  *
  *
Test Error = 0.03448275862068961
Learned classification tree model:
GBTClassificationModel (uid=gbtc_2397338fd084) with 10 trees
  Tree 0 (weight 1.0):
    If (feature 406 <= 20.0)
     If (feature 99 in {2.0})
      Predict: -1.0
     Else (feature 99 not in {2.0})
      Predict: 1.0
    Else (feature 406 > 20.0)
     Predict: -1.0
  Tree 1 (weight 0.1):
    If (feature 407 <= 0.0)
     If (feature 125 <= 254.0)
      If (feature 539 <= 251.0)
       Predict: 0.47681168808847024
      Else (feature 539 > 251.0)
       Predict: 0.47681168808847035
     Else (feature 125 > 254.0)
      Predict: -0.4768116880884694
    Else (feature 407 > 0.0)
     If (feature 323 <= 109.0)
      Predict: -0.47681168808847024
     Else (feature 323 > 109.0)
      Predict: -0.47681168808847035
  Tree 2 (weight 0.1):
    If (feature 434 <= 0.0)
     If (feature 238 <= 253.0)
      If (feature 413 <= 86.0)
       If (feature 126 <= 58.0)
        Predict: 0.4381935810427206
       Else (feature 126 > 58.0)
        Predict: 0.43819358104272055
      Else (feature 413 > 86.0)
       Predict: 0.43819358104272066
     Else (feature 238 > 253.0)
      Predict: -0.43819358104271977
    Else (feature 434 > 0.0)
     If (feature 323 <= 0.0)
      Predict: -0.4381935810427206
     Else (feature 323 > 0.0)
      Predict: -0.4381935810427206
  Tree 3 (weight 0.1):
    If (feature 489 <= 0.0)
     If (feature 568 <= 253.0)
      Predict: 0.40514968028459825
     Else (feature 568 > 253.0)
      Predict: -0.4051496802845982
    Else (feature 489 > 0.0)
     Predict: -0.4051496802845983
  Tree 4 (weight 0.1):
    If (feature 407 <= 0.0)
     If (feature 125 <= 254.0)
      If (feature 128 <= 141.0)
       If (feature 245 <= 63.0)
        Predict: 0.3765841318352991
       Else (feature 245 > 63.0)
        Predict: 0.37658413183529926
      Else (feature 128 > 141.0)
       Predict: 0.3765841318352994
     Else (feature 125 > 254.0)
      Predict: -0.3765841318352994
    Else (feature 407 > 0.0)
     If (feature 432 <= 61.0)
      If (feature 349 <= 7.0)
       Predict: -0.3765841318352991
      Else (feature 349 > 7.0)
       Predict: -0.37658413183529926
     Else (feature 432 > 61.0)
      Predict: -0.3765841318352994
  Tree 5 (weight 0.1):
    If (feature 407 <= 0.0)
     If (feature 125 <= 254.0)
      If (feature 272 <= 0.0)
       Predict: 0.35166478958101005
      Else (feature 272 > 0.0)
       Predict: 0.3516647895810101
     Else (feature 125 > 254.0)
      Predict: -0.3516647895810099
    Else (feature 407 > 0.0)
     Predict: -0.35166478958101
  Tree 6 (weight 0.1):
    If (feature 407 <= 0.0)
     If (feature 125 <= 254.0)
      If (feature 289 <= 48.0)
       If (feature 183 <= 253.0)
        Predict: 0.32974984655529926
       Else (feature 183 > 253.0)
        Predict: 0.32974984655529926
      Else (feature 289 > 48.0)
       Predict: 0.3297498465552993
     Else (feature 125 > 254.0)
      Predict: -0.3297498465552984
    Else (feature 407 > 0.0)
     If (feature 571 <= 225.0)
      If (feature 123 <= 32.0)
       Predict: -0.32974984655529926
      Else (feature 123 > 32.0)
       Predict: -0.32974984655529926
     Else (feature 571 > 225.0)
      Predict: -0.32974984655529943
  Tree 7 (weight 0.1):
    If (feature 406 <= 20.0)
     If (feature 568 <= 253.0)
      If (feature 183 <= 136.0)
       Predict: 0.3103372455197956
      Else (feature 183 > 136.0)
       Predict: 0.3103372455197957
     Else (feature 568 > 253.0)
      Predict: -0.31033724551979525
    Else (feature 406 > 20.0)
     If (feature 267 <= 53.0)
      If (feature 461 <= 252.0)
       Predict: -0.3103372455197956
      Else (feature 461 > 252.0)
       Predict: -0.3103372455197957
     Else (feature 267 > 53.0)
      If (feature 377 <= 126.0)
       If (feature 295 <= 253.0)
        Predict: -0.3103372455197956
       Else (feature 295 > 253.0)
        Predict: -0.3103372455197957
      Else (feature 377 > 126.0)
       Predict: -0.3103372455197957
  Tree 8 (weight 0.1):
    If (feature 406 <= 20.0)
     If (feature 238 <= 253.0)
      If (feature 155 <= 104.0)
       If (feature 180 <= 0.0)
        Predict: 0.2930291649125433
       Else (feature 180 > 0.0)
        Predict: 0.2930291649125433
      Else (feature 155 > 104.0)
       Predict: 0.2930291649125434
     Else (feature 238 > 253.0)
      Predict: -0.29302916491254294
    Else (feature 406 > 20.0)
     If (feature 378 <= 251.0)
      If (feature 269 <= 56.0)
       Predict: -0.2930291649125433
      Else (feature 269 > 56.0)
       Predict: -0.2930291649125434
     Else (feature 378 > 251.0)
      Predict: -0.29302916491254344
  Tree 9 (weight 0.1):
    If (feature 406 <= 20.0)
     If (feature 344 <= 253.0)
      If (feature 209 <= 34.0)
       If (feature 125 <= 0.0)
        Predict: 0.27750666438358246
       Else (feature 125 > 0.0)
        Predict: 0.2775066643835825
      Else (feature 209 > 34.0)
       If (feature 401 <= 72.0)
        Predict: 0.2775066643835825
       Else (feature 401 > 72.0)
        Predict: 0.2775066643835826
     Else (feature 344 > 253.0)
      Predict: -0.2775066643835826
    Else (feature 406 > 20.0)
     If (feature 266 <= 42.0)
      Predict: -0.2775066643835825
     Else (feature 266 > 42.0)
      If (feature 433 <= 0.0)
       Predict: -0.27750666438358246
      Else (feature 433 > 0.0)
       Predict: -0.27750666438358257
  *
  * */
}
