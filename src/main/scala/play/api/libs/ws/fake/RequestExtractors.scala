package play.api.libs.ws.fake

trait RequestExtractors {

  val GET = new RequestMethodExtractor("GET")

  val POST = new RequestMethodExtractor("POST")

  val PUT = new RequestMethodExtractor("PUT")

  val DELETE = new RequestMethodExtractor("DELETE")

  val HEAD = new RequestMethodExtractor("HEAD")

  val OPTIONS = new RequestMethodExtractor("OPTIONS")

  implicit class FakeRequestContext(val sc: StringContext) {
    val url = new {
      def unapplySeq(r: FakeRequest): Option[Seq[String]] = {
        val regexp = sc.parts.mkString("(.+)").r
        regexp.unapplySeq(r.url)
      }
    }
  }

  class RequestMethodExtractor(method: String) {
    def unapply(request: FakeRequest): Option[FakeRequest] = Some(request).filter(_.method.equalsIgnoreCase(method))
  }
}

object RequestExtractors extends RequestExtractors
