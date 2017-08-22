package play.api.libs.ws.fake

import java.net.URI

import play.api.libs.ws._

import scala.concurrent.duration.Duration
import scala.concurrent.{ExecutionContext, Future, duration}

case class StandaloneFakeWSRequest
(
  routes: PartialFunction[(String, String), Future[FakeResult]],
  method: String = "GET",
  url: String,
  queryString: Map[String, Seq[String]] = Map(),
  body: WSBody = EmptyBody,
  headers: Map[String, Seq[String]] = Map(),
  cookies: Seq[WSCookie] = Seq(),
  auth: Option[(String, String, WSAuthScheme)] = None,
  requestTimeout: Option[Int] = None,
  calc: Option[WSSignatureCalculator] = None
)(implicit ec: ExecutionContext) extends StandaloneWSRequest {

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

  override def sign(calc: WSSignatureCalculator): Self = copy(calc = Some(calc))

  override def withAuth(username: String, password: String, scheme: WSAuthScheme): Self =
    copy(auth = Some((username, password, scheme)))

  override def withHttpHeaders(headers: (String, String)*): Self = {
    val newHeaders = headers.foldLeft(Map[String, Seq[String]]()) {
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

  override def withRequestTimeout(timeout: duration.Duration): Self = timeout match {
    case Duration.Inf => copy(requestTimeout = None)
    case d =>
      val millis = d.toMillis
      val withinInteger = millis >= 0 && millis <= Int.MaxValue
      require(withinInteger, s"Request timeout must be between 0 and ${Int.MaxValue} milliseconds")
      copy(requestTimeout = Some(millis.toInt))
  }

  override def withRequestFilter(filter: WSRequestFilter): Self = this

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

  override def execute(): Future[Response] = for {
    result <- routes.lift((method, url)).getOrElse(throw new Exception(s"no route defined for $method $url"))
  } yield new StandaloneFakeWSResponse(result)

  override def stream(): Future[Response] = execute()

  private def withBodyAndContentType(wsBody: WSBody, contentType: String): Self = {
    if (headers.contains("Content-Type")) {
      withBody(wsBody)
    } else {
      withBody(wsBody).withHttpHeaders("Content-Type" -> contentType)
    }
  }
}
