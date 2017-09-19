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
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import org.f100ded.play.fakews._
import org.scalatest._
import play.api.libs.ws.JsonBodyWritables._

import scala.concurrent.duration.Duration
import scala.concurrent._
import scala.language.reflectiveCalls

/**
  * Tests MyApi HTTP client implementation
  */
class MyApiClientSpec extends AsyncFlatSpec with BeforeAndAfterAll with Matchers {
  
  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  import system.dispatcher

  behavior of "MyApiClient"

  it should "put access token to Authorization header" in {
    val accessToken = "fake_access_token"
    val ws = StandaloneFakeWSClient {
      case request @ GET(url"http://host/v1/foo/$id") =>
        // verify access token
        request.headers should contain ("Authorization" -> Seq(s"Bearer $accessToken"))
        
        // this is here just to demonstrate how you can use URL extractor
        id shouldBe "1"
        
        Ok(FakeAnswers.foo)
    }

    val api = new MyApiClient(ws, baseUrl = "http://host/", accessToken = accessToken)
    api.getFoo(1).map(_ => succeed)
  }

  // ... more tests

  override def afterAll(): Unit = {
    Await.result(system.terminate(), Duration.Inf)
  }

}
```

## Versions

This project supports the following versions of play-ws

| Fake WS version | Play-WS version |
|-----------------|-----------------|
| 1.0.x           | 1.0.x           |
| 1.1.x           | 1.1.x           |

