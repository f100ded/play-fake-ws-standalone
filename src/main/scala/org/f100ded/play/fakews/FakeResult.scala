package org.f100ded.play.fakews

import play.api.libs.ws.{BodyWritable, EmptyBody, WSBody, WSCookie}

import scala.language.implicitConversions

case class FakeResult
(
  status: Int,
  statusText: String,
  body: WSBody = EmptyBody,
  headers: Map[String, Seq[String]] = Map(),
  cookies: Seq[WSCookie] = Seq()
) {
  type Self = FakeResult

  def addHeaders(h: (String, String)*): Self = {
    val newHeaders = h.foldLeft(headers) {
      case (acc, (name, value)) => acc.get(name) match {
        case Some(values) => acc.updated(name, values :+ value)
        case None => acc + (name -> Seq(value))
      }
    }

    copy(headers = newHeaders)
  }

  def addCookies(c: WSCookie*): Self = copy(cookies = cookies ++ c)

  def apply[T](body: T)(implicit w: BodyWritable[T]): Self = copy(body = w.transform(body))
}

object FakeResult {
  implicit def result2routes(result: FakeResult): Routes = {
    case _ => result
  }
}
