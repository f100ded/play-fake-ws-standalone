import sbt._

object Dependencies {

  object Versions {
    val scala = "2.11.11"
    val crossScala = List(scala, "2.12.2")
  }

  object Libraries {
    val scalaTest: ModuleID = "org.scalatest" %% "scalatest" % "3.0.3"
    val scalaLogging: ModuleID = "com.typesafe.scala-logging" %% "scala-logging" % "3.7.2"
    val playWS: ModuleID = "com.typesafe.play" %% "play-ahc-ws-standalone" % "1.0.4"
  }
}