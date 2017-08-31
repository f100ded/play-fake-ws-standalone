package org.f100ded.play.fakews

import org.specs2.mutable._
import play.api.libs.ws.DefaultWSCookie

class FakeResultSpec extends Specification with FakeResults {
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
