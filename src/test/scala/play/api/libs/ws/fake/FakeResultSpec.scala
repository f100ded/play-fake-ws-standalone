package play.api.libs.ws.fake

import org.specs2.mutable._
import FakeResults._
import play.api.libs.ws.DefaultWSCookie

class FakeResultSpec extends Specification {
  "FakeResult" should {
    "add cookies" in {
      val result = Ok("string").addCookies(
        DefaultWSCookie("cookie_name1", "cookie_value1"),
        DefaultWSCookie("cookie_name2", "cookie_value2")
      )

      result.cookies.exists(_.name == "cookie_name1") must beTrue
      result.cookies.exists(_.name == "cookie_name2") must beTrue
    }

    "add headers" in {
      val result = Ok("string").addHeaders(
        "header1" -> "1",
        "header1" -> "2",
        "header2" -> "3"
      )

      result.headers.get("header1") must beSome(Seq("1", "2"))
      result.headers.get("header2") must beSome(Seq("3"))
    }
  }
}
