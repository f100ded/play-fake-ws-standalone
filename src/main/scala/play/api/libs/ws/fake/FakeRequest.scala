package play.api.libs.ws.fake

import akka.util.ByteString
import play.api.libs.ws.WSCookie

case class FakeRequest
(
  method: String = "GET",
  url: String,
  body: ByteString,
  headers: Map[String, Seq[String]],
  cookies: Seq[WSCookie] = Seq()
) {
  lazy val bodyAsString: String = body.decodeString(ByteString.UTF_8)
}