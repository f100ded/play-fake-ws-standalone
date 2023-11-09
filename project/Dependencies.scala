import sbt._

object Dependencies {

  object Versions {
    val scala = "3.3.1"
    val crossScala = List(scala, "2.13.12")
  }

  object Libraries {
    val scalaLogging: ModuleID = "com.typesafe.scala-logging" %% "scala-logging" % "3.9.5"
    val playWS: ModuleID = "org.playframework" %% "play-ahc-ws-standalone" % "3.0.0"
    val specs2: Seq[ModuleID] = Seq(
      "specs2-core",
      "specs2-junit",
    ).map("org.specs2" %% _ % "4.20.2")
  }
}
