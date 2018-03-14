name := "test_scala"

version := "1.0"

scalaVersion := "2.11.8"

scalaVersion := "2.11.8"
libraryDependencies += "org.apache.spark" % "spark-core_2.11" % "2.2.0"
libraryDependencies += "org.apache.spark" % "spark-mllib_2.11" % "2.2.0"
libraryDependencies += "org.apache.spark" % "spark-yarn_2.11" % "2.2.0"
libraryDependencies += "org.apache.spark" % "spark-hive_2.11" % "2.2.0"
libraryDependencies += "com.typesafe" % "config" % "1.3.1"
libraryDependencies += "com.github.scopt" % "scopt_2.10" % "3.3.0"
libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0"
libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.40"
libraryDependencies += "com.typesafe.play" %% "play-json" % "2.6.7"
//libraryDependencies += "com.typesafe.akka" % "akka-actor_2.11" % "2.5.6"
libraryDependencies += "net.liftweb" %% "lift-json" % "3.1.1"
libraryDependencies += "org.jfree" % "jfreechart" % "1.0.19"
libraryDependencies += "edu.umd" % "cloud9" % "1.5.0"
libraryDependencies += "info.bliki.wiki" % "bliki-core" % "3.0.19"
libraryDependencies += "org.apache.hadoop" % "hadoop-streaming" % "2.7.4"
libraryDependencies += "org.jfree" % "jcommon" % "1.0.23"
libraryDependencies += "org.apache.lucene" % "lucene-analyzers-common" % "6.6.0"
libraryDependencies += "org.apache.lucene" % "lucene-core" % "6.6.0"
libraryDependencies += "org.apache.spark" %% "spark-streaming" % "2.2.0" % "provided"
libraryDependencies += "org.apache.spark" %% "spark-streaming-flume-assembly" % "2.1.1"
libraryDependencies += "org.apache.spark" %% "spark-streaming-kafka" % "1.6.3"
libraryDependencies += "com.github.hirofumi" %% "xgboost4j-spark" % "0.7.0-p3"



dependencyOverrides ++= Set(
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.6.5",
  "io.netty" % "netty" % "3.10.3.Final")
libraryDependencies += "org.typelevel" %% "spire" % "0.14.1"
libraryDependencies += "org.scalatest" % "scalatest_2.11" % "3.0.0" % "test"
libraryDependencies += "com.github.nscala-time" %% "nscala-time" % "2.16.0"
//libraryDependencies += "org.scala-lang.modules" % "scala-xml" % "2.11"
