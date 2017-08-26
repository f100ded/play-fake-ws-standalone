package play.api.libs.ws.fake

import akka.stream.Materializer
import akka.stream.scaladsl.Source
import akka.util.ByteString
import play.api.libs.ws.{EmptyBody, InMemoryBody, SourceBody, WSBody}

import scala.concurrent.Await
import scala.concurrent.duration._

private[fake] object BodyUtils {

  def bodyAsBytes(body: WSBody)(implicit mat: Materializer): ByteString = body match {
    case EmptyBody => ByteString.empty
    case InMemoryBody(bytes) => bytes
    case SourceBody(source) => blockingSourceToBytes(source)
  }

  private def blockingSourceToBytes(source: Source[ByteString, _])
                                   (implicit mat: Materializer): ByteString = {
    import scala.concurrent.ExecutionContext.Implicits.global
    val result = source.runFold(ByteString.createBuilder) {
      case (acc, bytes) => acc.append(bytes)
    }.map(_.result())

    Await.result(result, 100.milliseconds)
  }

}
