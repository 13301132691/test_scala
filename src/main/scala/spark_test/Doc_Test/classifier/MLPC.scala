package spark_test.Doc_Test.classifier

import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator

object MLPC extends App{
  val spark = Spark.builder.master("local").appName("MLPC").getOrCreate()

  // Load the data stored in LIBSVM format as a DataFrame.
  val data = spark.read.format("libsvm")
    .load("data/mllib/sample_multiclass_classification_data.txt")

  // Split the data into train and test
  val splits = data.randomSplit(Array(0.6, 0.4), seed = 1234L)
  val train = splits(0)
  val test = splits(1)

  // specify layers for the neural network:
  // input layer of size 4 (features), two intermediate of size 5 and 4
  // and output of size 3 (classes)
  val layers = Array[Int](4,5,4,3)
  val trainer = new MLPC().setLayers(layers).setBlockSize(128).setSeed(1234L).setMaxIter(100)

  val model = trainer.fit(train)
  val res = model.transform(test)
  val predictionAndLabels = res.select("prediction", "label")
  val evaluator = new MulticlassClassificationEvaluator().setMetricName("accuracy")

  println("Test set accuracy = " + evaluator.evaluate(predictionAndLabels))
//Test set accuracy = 0.8627450980392157

}
