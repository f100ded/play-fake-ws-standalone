package play.api.libs.ws.fake

import play.api.libs.ws.WSCookie

case class FakeResult
(
  status: Int,
  statusText: String,
  headers: Map[String, Seq[String]],
  cookies: Seq[WSCookie],
  body: String
)
