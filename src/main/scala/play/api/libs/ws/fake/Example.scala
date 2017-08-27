package play.api.libs.ws.fake

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import play.api.libs.ws.DefaultWSCookie
import play.api.libs.ws.fake.FakeRequestExtractors._

import scala.concurrent.Await
import scala.concurrent.duration._

object Example extends App {
  implicit val system = ActorSystem()
  implicit val mat = ActorMaterializer()

  import FakeResults._

  val ws = StandaloneFakeWSClient {
    case req@POST(url"http://foo/bar/$id") =>
      assert(req.headers.getOrElse("Content-Type", Seq()).contains("text/plain"))
      assert(req.cookies.exists(_.name == "cookie"))
      assert(req.bodyAsString == "hello world")
      assert(id == "123")
      Ok("Hi")
  }

  val f = ws.url("http://foo/bar/123")
    .withCookies(DefaultWSCookie("cookie", "a"))
    .post("hello world")

  Await.result(f, 1.second).body

  system.terminate()
}
