package play.api.libs.ws.fake

import play.api.libs.ws.{WSBody, WSCookie}

case class FakeRequest
(
  method: String = "GET",
  url: String,
  body: WSBody,
  headers: Map[String, Seq[String]],
  cookies: Seq[WSCookie] = Seq()
)