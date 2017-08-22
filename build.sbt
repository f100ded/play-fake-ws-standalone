import Dependencies._

name := "play-ws-standalone-mock"

organization := "org.f100ded.play-ws-standalone-mock"

scalaVersion := Versions.scala

crossScalaVersions := Versions.crossScala

scalacOptions ++= List("-unchecked", "-deprecation", "-encoding", "UTF-8")

libraryDependencies ++= Seq(
  Libraries.playWS % "provided",
  Libraries.scalaTest % "test"
)

licenses := Seq("Apache 2.0 License" -> url("http://www.apache.org/licenses/LICENSE-2.0.html"))

homepage := Some(url("https://github.com/f100ded/play-ws-standalone-mock"))

publishTo := {
  if (isSnapshot.value) {
    Some(Opts.resolver.sonatypeSnapshots)
  } else {
    Some(Opts.resolver.sonatypeStaging)
  }
}

publishArtifact in Test := false

pomIncludeRepository := (_ => false)

pomExtra := {
  <scm>
    <url>https://github.com/f100ded/play-ws-standalone-mock</url>
    <connection>scm:git:git://github.com/f100ded/play-ws-standalone-mock.git</connection>
  </scm>
    <developers>
      <developer>
        <id>tartakynov</id>
        <name>Artem Tartakynov</name>
        <url>http://github.com/tartakynov</url>
      </developer>
    </developers>
}