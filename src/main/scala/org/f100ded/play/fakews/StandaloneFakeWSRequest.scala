package org.f100ded.play.fakews

import java.net.URI
import java.util.Base64

import akka.stream.Materializer
import com.typesafe.scalalogging.LazyLogging
import play.api.libs.ws._

import scala.concurrent.{Future, duration}

case class StandaloneFakeWSRequest
(
  routes: PartialFunction[FakeRequest, FakeResult],
  method: String = "GET",
  url: String,
  queryString: Map[String, Seq[String]] = Map(),
  body: WSBody = EmptyBody,
  headers: Map[String, Seq[String]] = Map(),
  cookies: Seq[WSCookie] = Seq(),
  auth: Option[(String, String, WSAuthScheme)] = None
)(implicit mat: Materializer) extends StandaloneWSRequest with LazyLogging {

  override type Self = StandaloneWSRequest
  override type Response = StandaloneWSResponse

  override lazy val uri: URI = {
    val enc = (p: String) => java.net.URLEncoder.encode(p, "utf-8")
    new java.net.URI(if (queryString.isEmpty) url else {
      val qs = (for {
        (n, vs) <- queryString
        v <- vs
      } yield s"${enc(n)}=${enc(v)}").mkString("&")
      s"$url?$qs"
    })
  }

  override lazy val contentType: Option[String] = this.headers
    .flatMap {
      case (name, values) if name.equalsIgnoreCase("Content-Type") => values
      case _ => Nil
    }.headOption

  override def followRedirects: Option[Boolean] = None

  override def virtualHost: Option[String] = None

  override def proxyServer: Option[WSProxyServer] = None

  override def sign(calc: WSSignatureCalculator): Self = {
    logger.warn(s"Request signature is not supported in play-fake-standalone-mock. Skipping")
    this
  }

  override def withAuth(username: String, password: String, scheme: WSAuthScheme): Self = scheme match {
    case WSAuthScheme.BASIC =>
      val authorization = Base64.getMimeEncoder().encodeToString(s"$username:$password".getBytes("UTF-8"))
      withHttpHeaders("Authorization" -> s"Basic: $authorization")
    case unsupported =>
      logger.warn(s"Auth scheme $unsupported is not supported in play-fake-standalone-mock. Skipping")
      this
  }

  override def withHttpHeaders(h: (String, String)*): Self = {
    val newHeaders = h.foldLeft(Map[String, Seq[String]]()) {
      case (acc, (name, value)) => acc + (name -> (value +: acc.getOrElse(name, Nil)))
    }

    copy(headers = contentType match {
      case Some(ct) => newHeaders.updated("Content-Type", Seq(ct))
      case None => newHeaders
    })
  }

  override def withQueryStringParameters(parameters: (String, String)*): Self = {
    val newQueryString = parameters.foldLeft(Map[String, Seq[String]]()) {
      case (params, (name, value)) => params + (name -> (value +: params.getOrElse(name, Nil)))
    }

    copy(queryString = newQueryString)
  }

  override def withCookies(cookies: WSCookie*): Self = copy(cookies = cookies)

  override def withFollowRedirects(follow: Boolean): Self = this

  override def withRequestTimeout(timeout: duration.Duration): Self = {
    logger.warn(s"Request timeout is not supported in play-fake-standalone-mock. Skipping")
    this
  }

  override def withRequestFilter(filter: WSRequestFilter): Self = {
    logger.warn(s"Request filter is not supported in play-fake-standalone-mock. Skipping")
    this
  }

  override def withVirtualHost(vh: String): Self = this

  override def withProxyServer(proxyServer: WSProxyServer): Self = this

  override def withMethod(method: String): Self = copy(method = method)

  override def withBody[T: BodyWritable](body: T): Self = {
    val writable = implicitly[BodyWritable[T]]
    withBodyAndContentType(writable.transform(body), writable.contentType)
  }

  override def get(): Future[Response] = execute("GET")

  override def patch[T: BodyWritable](body: T): Future[Response] = withBody(body).execute("PATCH")

  override def post[T: BodyWritable](body: T): Future[Response] = withBody(body).execute("POST")

  override def put[T: BodyWritable](body: T): Future[Response] = withBody(body).execute("PUT")

  override def delete(): Future[Response] = execute("DELETE")

  override def head(): Future[Response] = execute("HEAD")

  override def options(): Future[Response] = execute("OPTIONS")

  override def execute(method: String): Future[Response] = withMethod(method).execute()

  override def execute(): Future[Response] = {
    logger.debug(s"WS: $method $url")
    val result = routes
      .lift(fakeRequest)
      .getOrElse(throw new Exception(s"no route defined for $method $url"))
    Future.successful(new StandaloneFakeWSResponse(result))
  }

  lazy val fakeRequest: FakeRequest = FakeRequest(method, uri.toString, BodyUtils.bodyAsBytes(body), headers, cookies)

  override def stream(): Future[Response] = execute()

  override def calc: Option[WSSignatureCalculator] = None

  override def requestTimeout: Option[Int] = None

  private def withBodyAndContentType(wsBody: WSBody, contentType: String): Self = {
    if (headers.exists(_._1.equalsIgnoreCase("Content-Type"))) {
      copy(body = wsBody)
    } else {
      copy(body = wsBody).addHttpHeaders("Content-Type" -> contentType)
    }
  }
}