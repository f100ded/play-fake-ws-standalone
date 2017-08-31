package org.f100ded.play.fakews

trait FakeRequestExtractors {

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

  class RequestMethodExtractor(method: String) {
    def unapply(request: FakeRequest): Option[FakeRequest] = Some(request).filter(_.method.equalsIgnoreCase(method))
  }

}

object FakeRequestExtractors extends FakeRequestExtractors
