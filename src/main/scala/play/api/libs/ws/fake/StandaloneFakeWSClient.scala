package play.api.libs.ws.fake

import play.api.libs.ws.{BodyWritable, StandaloneWSClient, StandaloneWSRequest}

import scala.concurrent.ExecutionContext

class StandaloneFakeWSClient(implicit ec: ExecutionContext) extends StandaloneWSClient {
  override def underlying[T]: T = ???

  override def url(url: String): StandaloneWSRequest = ???

  override def close(): Unit = ???
}

object StandaloneFakeWSClient {
  def apply[T](handler: PartialFunction[(String, String), FakeResult])
              (implicit w: BodyWritable[T]): StandaloneWSClient = {
    ???
  }
}