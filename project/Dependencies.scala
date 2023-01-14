import sbt._

object Dependencies {

  object Versions {
    val scala = "3.2.1"
    val crossScala = List(scala, "2.13.7")
  }

  object Libraries {
    val scalaLogging: ModuleID = "com.typesafe.scala-logging" %% "scala-logging" % "3.9.4"
    val playWS: ModuleID = "com.typesafe.play" %% "play-ahc-ws-standalone" % "2.2.0-M2"
    val specs2: Seq[ModuleID] = Seq(
      "specs2-core",
      "specs2-junit",
    ).map("org.specs2" %% _ % "4.19.0")
  }
}
