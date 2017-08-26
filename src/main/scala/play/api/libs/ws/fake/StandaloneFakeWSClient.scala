package play.api.libs.ws.fake

import akka.stream.Materializer
import play.api.libs.ws.{StandaloneWSClient, StandaloneWSRequest}

class StandaloneFakeWSClient(routes: PartialFunction[FakeRequest, FakeResult])
                            (implicit mat: Materializer) extends StandaloneWSClient {
  override def underlying[T]: T = this.asInstanceOf[T]

  override def url(url: String): StandaloneWSRequest = StandaloneFakeWSRequest(routes = routes, url = url)

  override def close(): Unit = Unit
}

object StandaloneFakeWSClient {
  def apply(routes: PartialFunction[FakeRequest, FakeResult])
           (implicit mat: Materializer): StandaloneWSClient = {
    new StandaloneFakeWSClient(routes)
  }
}