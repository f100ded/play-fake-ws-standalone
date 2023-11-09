package org.f100ded.play.fakews

import java.net.URI

import org.apache.pekko.stream.Materializer
import org.apache.pekko.stream.scaladsl.Source
import org.apache.pekko.util.ByteString
import play.api.libs.ws.ahc.AhcUtilities
import play.api.libs.ws.{DefaultBodyReadables, StandaloneWSResponse, WSCookie}

class StandaloneFakeWSResponse(val uri: URI, result: FakeResult)
                              (implicit mat: Materializer) extends StandaloneWSResponse
  with DefaultBodyReadables
  with AhcUtilities {
  override def headers: Map[String, Seq[String]] = result.headers

  override def underlying[T]: T = result.asInstanceOf[T]

  override def status: Int = result.status

  override def statusText: String = result.statusText

  override def cookies: Seq[WSCookie] = result.cookies

  override def cookie(name: String): Option[WSCookie] = result.cookies.find(_.name == name)

  override lazy val body: String = bodyAsBytes.decodeString(ByteString.UTF_8)

  override lazy val bodyAsBytes: ByteString = BodyUtils.bodyAsBytes(result.body)

  override lazy val bodyAsSource: Source[ByteString, _] = Source.single(bodyAsBytes)
}
