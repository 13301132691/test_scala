package spark_test.Doc_Test.classifier

object LR extends App{
  val spark = Spark.builder.master("local").appName("LR").getOrCreate()
  val train = spark.read.format("libsvm").load("data/mllib/sample_libsvm_data.txt")
  val lr = new LR().setMaxIter(100).setRegParam(0.3).setElasticNetParam(0.8)
  val mlr = new LR().setMaxIter(100).setRegParam(0.3).setElasticNetParam(0.8).setFamily("multinomial")
  val lrModel = lr.fit(train)
  val mlrModel = mlr.fit(train)

  // Print the coefficients and intercept for logistic regression
  println(s"Coefficients: ${lrModel.coefficientMatrix} Intercept: ${lrModel.intercept}")
/*
*feature为692维，故有692个coefficient,但不明白为何截距只有一个（这里只是线性的叠加，并不是真的在多维空间中）
* Coefficients: 1 x 692 CSCMatrix
(0,244) -7.353983524188197E-5
(0,263) -9.102738505589466E-5
(0,272) -1.9467430546904298E-4
(0,300) -2.0300642473486668E-4
(0,301) -3.1476183314863995E-5
(0,328) -6.842977602660743E-5
(0,350) 1.5883626898239883E-5
(0,351) 1.4023497091372047E-5
(0,378) 3.5432047524968605E-4
(0,379) 1.1443272898171087E-4
(0,405) 1.0016712383666666E-4
(0,406) 6.014109303795481E-4
(0,407) 2.840248179122762E-4
(0,428) -1.1541084736508837E-4
(0,433) 3.85996886312906E-4
(0,434) 6.35019557424107E-4
(0,455) -1.1506412384575676E-4
(0,456) -1.5271865864986808E-4
(0,461) 2.804933808994214E-4
(0,462) 6.070117471191634E-4
(0,483) -2.008459663247437E-4
(0,484) -1.421075579290126E-4
(0,489) 2.739010341160883E-4
(0,490) 2.7730456244968115E-4
(0,496) -9.838027027269332E-5
(0,511) -3.808522443517704E-4
(0,512) -2.5315198008555033E-4
(0,517) 2.7747714770754307E-4
(0,539) -2.443619763919199E-4
(0,540) -0.0015394744687597765
(0,568) -2.3073328411331293E-4
Intercept: 0.22456315961250325
 */
  // Print the coefficients and intercepts for logistic regression with multinomial family
  println(s"Multinomial coefficients: ${mlrModel.coefficientMatrix}")
  println(s"Multinomial intercepts: ${mlrModel.interceptVector}")

/*
*
* 这里分类有两个直线，可分别判断：是0，是1，非0非1
* Multinomial coefficients: 2 x 692 CSCMatrix
(0,244) 4.290365458958277E-5
(1,244) -4.290365458958294E-5
(0,263) 6.488313287833108E-5
(1,263) -6.488313287833092E-5
(0,272) 1.2140666790834663E-4
(1,272) -1.2140666790834657E-4
(0,300) 1.3231861518665612E-4
(1,300) -1.3231861518665607E-4
(0,350) -6.775444746760509E-7
(1,350) 6.775444746761932E-7
(0,351) -4.899237909429297E-7
(1,351) 4.899237909430322E-7
(0,378) -3.5812102770679596E-5
(1,378) 3.581210277067968E-5
(0,379) -2.3539704331222065E-5
(1,379) 2.353970433122204E-5
(0,405) -1.90295199030314E-5
(1,405) 1.90295199030314E-5
(0,406) -5.626696935778909E-4
(1,406) 5.626696935778912E-4
(0,407) -5.121519619099504E-5
(1,407) 5.1215196190995074E-5
(0,428) 8.080614545413342E-5
(1,428) -8.080614545413331E-5
(0,433) -4.256734915330487E-5
(1,433) 4.256734915330495E-5
(0,434) -7.080191510151425E-4
(1,434) 7.080191510151435E-4
(0,455) 8.094482475733589E-5
(1,455) -8.094482475733582E-5
(0,456) 1.0433687128309833E-4
(1,456) -1.0433687128309814E-4
(0,461) -5.4466605046259246E-5
(1,461) 5.4466605046259286E-5
(0,462) -5.667133061990392E-4
(1,462) 5.667133061990392E-4
(0,483) 1.2495896045528374E-4
(1,483) -1.249589604552838E-4
(0,484) 9.810519424784944E-5
(1,484) -9.810519424784941E-5
(0,489) -4.88440907254626E-5
(1,489) 4.8844090725462606E-5
(0,490) -4.324392733454803E-5
(1,490) 4.324392733454811E-5
(0,496) 6.903351855620161E-5
(1,496) -6.90335185562012E-5
(0,511) 3.946505594172827E-4
(1,511) -3.946505594172831E-4
(0,512) 2.621745995919226E-4
(1,512) -2.621745995919226E-4
(0,517) -4.459475951170906E-5
(1,517) 4.459475951170901E-5
(0,539) 2.5417562428184555E-4
(1,539) -2.5417562428184555E-4
(0,540) 5.271781246228031E-4
(1,540) -5.271781246228032E-4
(0,568) 1.860255150352447E-4
(1,568) -1.8602551503524485E-4
Multinomial intercepts: [-0.12065879445860686,0.12065879445860686]
*
* */

  val trainingSummary = lrModel.summary

  // Obtain the objective per iteration.
  val objectiveHistory = trainingSummary.objectiveHistory
  println("objectiveHistory:")
  objectiveHistory.foreach(loss => println(loss))

  // Obtain the metrics useful to judge performance on test data.
  // We cast the summary to a BinaryLogisticRegressionSummary since the problem is a
  // binary classification problem.
  val binarySummary = trainingSummary.asInstanceOf[BLRS]
  val roc = binarySummary.roc

  roc.show()
  println(s"areaUnderROC: ${binarySummary.areaUnderROC}")


  //看不懂
  // Set the model threshold to maximize F-Measure
//  val fMeasure = binarySummary.fMeasureByThreshold
//  val maxFMeasure = fMeasure.select(max("F-Measure")).head().getDouble(0)
//  val bestThreshold = fMeasure.where($"F-Measure" === maxFMeasure)
//    .select("threshold").head().getDouble(0)
//  lrModel.setThreshold(bestThreshold)
}
