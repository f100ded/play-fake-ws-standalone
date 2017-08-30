package play.api.libs.ws.fake

import akka.stream.Materializer
import play.api.libs.ws.{StandaloneWSClient, StandaloneWSRequest}

import scala.language.implicitConversions

class StandaloneFakeWSClient(implicit mat: Materializer) extends StandaloneWSClient
  with FakeResults with FakeRequestExtractors {

  type Routes = PartialFunction[FakeRequest, FakeResult]

  var routes: Routes = PartialFunction.empty[FakeRequest, FakeResult]

  override def underlying[T]: T = this.asInstanceOf[T]

  override def url(url: String): StandaloneWSRequest = StandaloneFakeWSRequest(routes = routes, url = url)

  override def close(): Unit = Unit

  implicit def result2routes(result: FakeResult): Routes = {
    case _ => result
  }
}