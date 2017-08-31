package org.f100ded.play

package object fakews {

  type Routes = PartialFunction[FakeRequest, FakeResult]

  // Method extractors

  val GET = new RequestMethodExtractor("GET")

  val POST = new RequestMethodExtractor("POST")

  val PUT = new RequestMethodExtractor("PUT")

  val DELETE = new RequestMethodExtractor("DELETE")

  val HEAD = new RequestMethodExtractor("HEAD")

  val OPTIONS = new RequestMethodExtractor("OPTIONS")

  val PATCH = new RequestMethodExtractor("PATCH")

  implicit class FakeRequestContext(val sc: StringContext) {
    val url = new {
      def unapplySeq(r: FakeRequest): Option[Seq[String]] = {
        val regexp = sc.parts.mkString("(.+)").r
        regexp.unapplySeq(r.url)
      }
    }
  }

  // Results

  val Ok = FakeResult(200, "OK")

  val Created = FakeResult(201, "Created")

  val Accepted = FakeResult(202, "Accepted")

  val NonAuthoritativeInformation = FakeResult(203, "Non-Authoritative Information")

  val NoContent = FakeResult(204, "No Content")

  val NotModified = FakeResult(304, "Not Modified")

  val BadRequest = FakeResult(400, "400 Bad Request")

  val Unauthorized = FakeResult(401, "401 Unauthorized")

  val PaymentRequired = FakeResult(402, "402 Payment Required")

  val Forbidden = FakeResult(403, "403 Forbidden")

  val NotFound = FakeResult(404, "404 Not Found")

  val MethodNotAllowed = FakeResult(405, "405 Method Not Allowed")

  val NotAcceptable = FakeResult(406, "406 Not Acceptable")

  val RequestTimeout = FakeResult(408, "408 Request Timeout")

  val Conflict = FakeResult(409, "409 Conflict")

  val Gone = FakeResult(410, "410 Gone")

  val PreconditionFailed = FakeResult(412, "412 Precondition Failed")

  val EntityTooLarge = FakeResult(413, "413 Request Entity Too Large")

  val UriTooLong = FakeResult(414, "414 Request-URI Too Long")

  val UnsupportedMediaType = FakeResult(415, "415 Unsupported Media Type")

  val ExpectationFailed = FakeResult(417, "417 Expectation Failed")

  val UnprocessableEntity = FakeResult(422, "422 Unprocessable Entity")

  val Locked = FakeResult(423, "423 Locked")

  val FailedDependency = FakeResult(424, "424 Failed Dependency")

  val TooManyRequests = FakeResult(429, "429 Too Many Requests")

  val InternalServerError = FakeResult(500, "500 Internal Server Error")

  val NotImplemented = FakeResult(501, "501 Not Implemented")

  val BadGateway = FakeResult(502, "502 Bad Gateway")

  val ServiceUnavailable = FakeResult(503, "503 Service Unavailable")

  val GatewayTimeout = FakeResult(504, "504 Gateway Timeout")

  val HttpVersionNotSupported = FakeResult(505, "505 HTTP Version Not Supported")

  val InsufficientStorage = FakeResult(507, "507 Insufficient Storage")
}
