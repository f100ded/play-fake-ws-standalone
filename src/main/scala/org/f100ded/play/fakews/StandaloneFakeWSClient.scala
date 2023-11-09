package org.f100ded.play.fakews

import org.apache.pekko.stream.Materializer
import play.api.libs.ws.{StandaloneWSClient, StandaloneWSRequest}

import scala.language.implicitConversions

class StandaloneFakeWSClient(routes: Routes)
                            (implicit mat: Materializer) extends StandaloneWSClient {

  override def underlying[T]: T = this.asInstanceOf[T]

  override def url(url: String): StandaloneWSRequest = StandaloneFakeWSRequest(routes = routes, url = url)

  override def close(): Unit = ()
}

object StandaloneFakeWSClient {
  def apply(routes: Routes)(implicit mat: Materializer): StandaloneFakeWSClient = new StandaloneFakeWSClient(routes)
}
