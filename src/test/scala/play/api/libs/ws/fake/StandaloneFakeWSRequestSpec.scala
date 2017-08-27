package play.api.libs.ws.fake

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import org.specs2.mutable._
import org.specs2.specification.core.Fragments
import play.api.libs.ws.DefaultBodyWritables

class StandaloneFakeWSRequestSpec extends Specification with DefaultBodyWritables {
  override def map(fragments: => Fragments): Fragments = fragments ^ step(afterAll())

  implicit val system = ActorSystem()

  implicit val mat = ActorMaterializer()

  val ws = StandaloneFakeWSClient {
    case _ => FakeResults.Ok
  }

  "POST" should {
    "no Content-Type if body is empty" in {
      val request = ws.url("http://localhost").asInstanceOf[StandaloneFakeWSRequest]
      request.fakeRequest.headers.get("Content-Type") must beNone
    }

    "set Content-Type to text/plain if body is text" in {
      val request = ws.url("http://localhost")
        .withBody("hello world")
        .asInstanceOf[StandaloneFakeWSRequest]
      request.fakeRequest.headers.get("Content-Type") must beSome(Seq("text/plain"))
    }
  }

  protected def afterAll(): Unit = {
    system.terminate()
  }
}
