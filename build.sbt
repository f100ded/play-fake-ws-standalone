import Dependencies._

name := "play-fake-ws-standalone"

organization := "org.f100ded.play"

scalaVersion := Versions.scala

crossScalaVersions := Versions.crossScala

scalacOptions ++= List("-unchecked", "-deprecation", "-encoding", "UTF-8")

libraryDependencies ++= Seq(
  Libraries.playWS % Provided,
  Libraries.scalaLogging
) ++ Libraries.specs2.map(_ % Test)

licenses := Seq("Apache 2.0 License" -> url("http://www.apache.org/licenses/LICENSE-2.0.html"))

homepage := Some(url("https://github.com/f100ded/play-fake-ws-standalone"))

publishTo := {
  if (isSnapshot.value) {
    Some(Opts.resolver.sonatypeSnapshots)
  } else {
    Some(Opts.resolver.sonatypeStaging)
  }
}

publishMavenStyle := true

publishArtifact in Test := false

pomIncludeRepository := (_ => false)

pomExtra := {
  <scm>
    <url>https://github.com/f100ded/play-fake-ws-standalone</url>
    <connection>scm:git:git://github.com/f100ded/play-fake-ws-standalone.git</connection>
  </scm>
    <developers>
      <developer>
        <id>tartakynov</id>
        <name>Artem Tartakynov</name>
        <url>http://github.com/tartakynov</url>
      </developer>
    </developers>
}

useGpg := true
