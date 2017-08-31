import sbt._

object Dependencies {

  object Versions {
    val scala = "2.11.11"
    val crossScala = List(scala, "2.12.2")
  }

  object Libraries {
    val scalaLogging: ModuleID = "com.typesafe.scala-logging" %% "scala-logging" % "3.7.2"
    val playWS: ModuleID = "com.typesafe.play" %% "play-ahc-ws-standalone" % "1.0.4"
    val specs2: Seq[ModuleID] = Seq(
      "specs2-core",
      "specs2-junit",
      "specs2-mock"
    ).map("org.specs2" %% _ % "3.8.6")
  }
}