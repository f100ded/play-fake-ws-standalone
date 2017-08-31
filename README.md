[![Build status](https://travis-ci.org/f100ded/play-fake-ws-standalone.svg?branch=master)](https://travis-ci.org/f100ded/play-fake-ws-standalone) [![Coverage Status](https://coveralls.io/repos/f100ded/play-fake-ws-standalone/badge.svg?branch=master&service=github)](https://coveralls.io/github/f100ded/play-fake-ws-standalone?branch=master)

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
import org.f100ded.play.fakews._

val ws = StandaloneFakeWSClient {

  case req @ GET(url"http://web-service/stores/$storeId/orders") =>
    assert(req.headers.contains("Authorization"))
    assert(req.cookies.exists(_.name == "session_id"))
    assert(storeId == "123")
    Ok(fakeOrdersJson)
  
  case req @ POST(url"http://web-service/stores/$storeId/orders") =>
    val order = Json.parse(req.bodyAsString).as[Order]
    assert(order.id == 234)
    assert(order.items.count == 3)
    Created(Json.toJson(order)).addHeaders(
      "Location" -> s"http://web-service/stores/$storeId/orders/${order.id}"
    )

}

val client = new MyRESTClient(ws)
// ... test the client
```