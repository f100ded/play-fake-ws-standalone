package play.api.libs.ws.fake

import akka.util.ByteString
import play.api.libs.ws.WSCookie

case class FakeRequest
(
  method: String,
  url: String,
  body: ByteString = ByteString.empty,
  headers: Map[String, Seq[String]] = Map(),
  cookies: Seq[WSCookie] = Seq()
) {
  lazy val bodyAsString: String = body.decodeString(ByteString.UTF_8)
}