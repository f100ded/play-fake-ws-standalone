package org.f100ded.play.fakews

import org.apache.pekko.util.ByteString
import play.api.libs.ws.{WSCookie, WSProxyServer}

case class FakeRequest
(
  method: String,
  url: String,
  body: ByteString = ByteString.empty,
  headers: Map[String, Seq[String]] = Map(),
  cookies: Seq[WSCookie] = Seq(),
  proxyServer: Option[WSProxyServer] = None
) {
  lazy val bodyAsString: String = body.decodeString(ByteString.UTF_8)
}
