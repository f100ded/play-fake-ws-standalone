package play.api.libs.ws.fake

import akka.stream.scaladsl.Source
import akka.util.ByteString
import play.api.libs.ws.ahc.AhcUtilities
import play.api.libs.ws.{DefaultBodyReadables, StandaloneWSResponse, WSCookie}

class StandaloneFakeWSResponse(result: FakeResult) extends StandaloneWSResponse
  with DefaultBodyReadables
  with AhcUtilities {
  override def headers: Map[String, Seq[String]] = result.headers

  override def underlying[T]: T = result.asInstanceOf[T]

  override def status: Int = result.status

  override def statusText: String = result.statusText

  override def cookies: Seq[WSCookie] = result.cookies

  override def cookie(name: String): Option[WSCookie] = result.cookies.find(_.name == name)

  override def body: String = result.body

  override lazy val bodyAsBytes: ByteString = ByteString.fromString(result.body)

  override lazy val bodyAsSource: Source[ByteString, _] = Source.single(bodyAsBytes)
}