package play.api.libs.ws.fake

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import org.specs2.concurrent.ExecutionEnv
import org.specs2.mutable._
import org.specs2.specification.core.Fragments
import play.api.libs.ws.DefaultBodyWritables

class StandaloneFakeWSRequestSpec extends Specification with DefaultBodyWritables with FakeRequestExtractors {
  override def map(fragments: => Fragments): Fragments = fragments ^ step(afterAll())

  implicit val system = ActorSystem()

  implicit val mat = ActorMaterializer()

  "StandaloneFakeWSRequest" should {

    "simulate HTTP methods correctly" in { implicit ee: ExecutionEnv =>
      val ws = StandaloneFakeWSClient {
        case GET(url"http://localhost/get") => FakeResults.Ok("get")
        case POST(url"http://localhost/post") => FakeResults.Ok("post")
        case PUT(url"http://localhost/put") => FakeResults.Ok("put")
        case HEAD(url"http://localhost/head") => FakeResults.Ok("head")
        case OPTIONS(url"http://localhost/options") => FakeResults.Ok("options")
      }

      ws.url("http://localhost/get").get.map(_.body) must beEqualTo("get").await
      ws.url("http://localhost/post").post("").map(_.body) must beEqualTo("post").await
      ws.url("http://localhost/put").put("").map(_.body) must beEqualTo("put").await
      ws.url("http://localhost/head").head.map(_.body) must beEqualTo("head").await
      ws.url("http://localhost/options").options.map(_.body) must beEqualTo("options").await
    }

    "no Content-Type if body is empty" in {
      val ws = StandaloneFakeWSClient { case _ => FakeResults.Ok }
      val r = ws.url("http://localhost/").asInstanceOf[StandaloneFakeWSRequest]
      r.fakeRequest.headers.get("Content-Type") must beNone
    }

    "set Content-Type to text/plain if body is text" in {
      val ws = StandaloneFakeWSClient { case _ => FakeResults.Ok }
      val r = ws.url("http://localhost/")
        .withBody("hello world")
        .asInstanceOf[StandaloneFakeWSRequest]
      r.fakeRequest.headers.get("Content-Type") must beSome(Seq("text/plain"))
    }

    "add query string params" in {
      val ws = StandaloneFakeWSClient { case _ => FakeResults.Ok }
      val r = ws.url("http://localhost/")
        .withQueryStringParameters(
          "a" -> "1",
          "b" -> "2"
        ).asInstanceOf[StandaloneFakeWSRequest]
      r.fakeRequest.url must contain("a=1")
      r.fakeRequest.url must contain("b=2")
    }
  }

  protected def afterAll(): Unit = {
    system.terminate()
  }
}
