import sbt._

object Dependencies {

  object Versions {
    val scala = "2.11.12"
    val crossScala = List(scala, "2.12.6")
  }

  object Libraries {
    val scalaLogging: ModuleID = "com.typesafe.scala-logging" %% "scala-logging" % "3.9.0"
    val playWS: ModuleID = "com.typesafe.play" %% "play-ahc-ws-standalone" % "2.0.0-M4"
    val specs2: Seq[ModuleID] = Seq(
      "specs2-core",
      "specs2-junit",
      "specs2-mock"
    ).map("org.specs2" %% _ % "3.8.6")
  }
}