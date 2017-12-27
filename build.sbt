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
libraryDependencies += "com.typesafe.akka" % "akka-actor_2.12" % "2.5.6"
libraryDependencies += "net.liftweb" %% "lift-json" % "3.1.1"

dependencyOverrides ++= Set(
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.6.5",
  "io.netty" % "netty" % "3.10.3.Final")
libraryDependencies += "org.typelevel" %% "spire" % "0.14.1"
libraryDependencies += "org.scalatest" % "scalatest_2.11" % "3.0.0" % "test"
libraryDependencies += "com.github.nscala-time" %% "nscala-time" % "2.16.0"
//libraryDependencies += "org.scala-lang.modules" % "scala-xml" % "2.11"
