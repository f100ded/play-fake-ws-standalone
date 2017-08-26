package play.api.libs.ws.fake

import play.api.libs.ws.{StandaloneWSClient, StandaloneWSRequest}

import scala.concurrent.Await
import scala.concurrent.duration._

class StandaloneFakeWSClient(routes: PartialFunction[FakeRequest, FakeResult]) extends StandaloneWSClient {
  override def underlying[T]: T = this.asInstanceOf[T]

  override def url(url: String): StandaloneWSRequest = StandaloneFakeWSRequest(routes = routes, url = url)

  override def close(): Unit = Unit
}

object StandaloneFakeWSClient {
  def apply[T](routes: PartialFunction[FakeRequest, FakeResult]): StandaloneWSClient = {
    new StandaloneFakeWSClient(routes)
  }
}

object Example extends App {
  val ws: StandaloneWSClient = StandaloneFakeWSClient {
    case req if req.url == "http://localhost" =>
      assert(req.headers.get("Content-Type").contains(Seq("text/plain")))
      FakeResult(200, "", Map(), Seq(), "Hi!")
  }

  val f = ws.url("http://localhost").post("hello world")
  val r = Await.result(f, 1.second).body
  println(r)
}