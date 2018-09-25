[![Build status](https://travis-ci.org/f100ded/play-fake-ws-standalone.svg?branch=master)](https://travis-ci.org/f100ded/play-fake-ws-standalone)
[![Maven](https://img.shields.io/maven-central/v/org.f100ded.play/play-fake-ws-standalone_2.12.svg)](http://mvnrepository.com/artifact/org.f100ded.play/play-fake-ws-standalone_2.12)

# Fake Play WS Standalone

Fake Play WS Standalone is a mock library implementing Play WS Standalone interface. It can be used for testing of your HTTP clients without starting a web service.

## Usage

To get started, add the following dependency into your project:
```scala
libraryDependencies += "org.f100ded.play" %% "play-fake-ws-standalone" % "LATEST_VERSION"
```
Replace LATEST_VERSION with the actual release version from [the releases page](https://github.com/f100ded/play-fake-ws-standalone/releases).

And then you can start using the library:
```scala
package com.f100ded.play.example

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.f100ded.play.example.model.Bar
import org.f100ded.play.fakews._
import org.scalatest._
import play.api.libs.json.Json
import play.api.libs.ws.DefaultBodyWritables._
import play.api.libs.ws.JsonBodyWritables._

import scala.concurrent._
import scala.concurrent.duration.Duration
import scala.language.reflectiveCalls

/**
  * Tests FooApi HTTP client implementation
  */
class WsFooApiClientTest extends AsyncFunSuite with BeforeAndAfterAll with Matchers {

  implicit val system: ActorSystem = ActorSystem()
  implicit val mat: ActorMaterializer = ActorMaterializer()
  import system.dispatcher

  private val DUMMY_URL = "http://host/"
  private val DUMMY_BAR = Bar(1, "bar", 0)
  private val DUMMY_ACCESS_TOKEN = "fake_access_token"

  test("getBar: normal flow") {
    val ws = StandaloneFakeWSClient {
      case request@GET(url"http://host/bars/$id") =>
        id shouldBe "1"
        request.headers("Authorization") shouldBe Seq(s"Bearer $DUMMY_ACCESS_TOKEN")
        Ok(Json.toJson(DUMMY_BAR))
    }

    val api = new WsFooApiClient(ws, DUMMY_URL, DUMMY_ACCESS_TOKEN)
    api.getBar(1).map { result =>
      result shouldBe Some(DUMMY_BAR)
    }
  }

  test("getBar: gracefully handle 404") {
    val ws = StandaloneFakeWSClient {
      case GET(url"""http://host/bars/[\d]+""") =>
        NotFound
    }

    val api = new WsFooApiClient(ws, DUMMY_URL, DUMMY_ACCESS_TOKEN)
    api.getBar(1).map { result =>
      result shouldBe None
    }
  }

  test("getBar: gracefully handle unexpected status") {
    val ws = StandaloneFakeWSClient(InternalServerError)
    val api = new WsFooApiClient(ws, DUMMY_URL, DUMMY_ACCESS_TOKEN)
    api.getBar(1).failed.map {
      case e: FooApiClientException =>
        e.status shouldBe 500
    }
  }

  test("getBar: gracefully handle unexpected response body") {
    val ws = StandaloneFakeWSClient(Ok("This should not be here"))
    val api = new WsFooApiClient(ws, DUMMY_URL, DUMMY_ACCESS_TOKEN)
    api.getBar(1).failed.map {
      case e: FooApiClientException =>
        e.status shouldBe 200
    }
  }

  // ... more tests

  override def afterAll(): Unit = {
    Await.result(system.terminate(), Duration.Inf)
  }

}
```

More examples you can find in [play-fake-ws-standalone-example](https://github.com/f100ded/play-fake-ws-standalone-example) 

## Versions

This project supports the following versions of play-ws

| Fake WS version | Play-WS version |
|-----------------|-----------------|
| 1.0.x           | 1.0.x           |
| 1.1.x           | 1.1.x           |
| 2.0.x           | 2.0.x           |
