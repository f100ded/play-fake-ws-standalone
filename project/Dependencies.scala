import sbt._

object Dependencies {

  object Versions {
    val scala = "2.12.15"
    val crossScala = List(scala, "2.13.7")
  }

  object Libraries {
    val scalaLogging: ModuleID = "com.typesafe.scala-logging" %% "scala-logging" % "3.9.4"
    val playWS: ModuleID = "com.typesafe.play" %% "play-ahc-ws-standalone" % "2.1.3"
    val specs2: Seq[ModuleID] = Seq(
      "specs2-core",
      "specs2-junit",
      "specs2-mock"
    ).map("org.specs2" %% _ % "4.13.1")
  }
}
