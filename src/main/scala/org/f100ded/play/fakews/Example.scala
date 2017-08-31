package org.f100ded.play.fakews

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import play.api.libs.ws.DefaultWSCookie

import scala.concurrent.Await
import scala.concurrent.duration._

object Example extends App {
  implicit val system = ActorSystem()
  implicit val mat = ActorMaterializer()

  val ws = StandaloneFakeWSClient {
    case req@POST(url"http://foo/bar/$id") =>
      assert(req.headers.getOrElse("Content-Type", Seq()).contains("text/plain"))
      assert(req.cookies.exists(_.name == "cookie"))
      assert(req.bodyAsString == "hello world")
      assert(id == "123")
      Ok("Hi")
  }

  val f = ws.url("http://foo/get")
    .withCookies(DefaultWSCookie("cookie", "a"))
    .get

  Await.result(f, 1.second).body

  system.terminate()
}
