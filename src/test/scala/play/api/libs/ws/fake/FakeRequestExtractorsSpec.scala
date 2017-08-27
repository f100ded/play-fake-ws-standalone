package play.api.libs.ws.fake

import org.specs2.mutable._
import play.api.libs.ws.fake.FakeRequestExtractors._

class FakeRequestExtractorsSpec extends Specification {
  "extractors" should {
    "extract GET method" in {
      val request = FakeRequest("GET", "http://localhost")
      GET.unapply(request) must beSome(request)
      POST.unapply(request) must beNone
    }

    "extract POST method" in {
      val request = FakeRequest("POST", "http://localhost")
      POST.unapply(request) must beSome(request)
      GET.unapply(request) must beNone
    }

    "extract PUT" in {
      val request = FakeRequest("PUT", "http://localhost")
      PUT.unapply(request) must beSome(request)
      POST.unapply(request) must beNone
    }

    "extract DELETE" in {
      val request = FakeRequest("DELETE", "http://localhost")
      DELETE.unapply(request) must beSome(request)
      POST.unapply(request) must beNone
    }

    "extract OPTIONS" in {
      val request = FakeRequest("OPTIONS", "http://localhost")
      OPTIONS.unapply(request) must beSome(request)
      POST.unapply(request) must beNone
    }

    "extract HEAD" in {
      val request = FakeRequest("HEAD", "http://localhost")
      HEAD.unapply(request) must beSome(request)
      POST.unapply(request) must beNone
    }
  }

  "string interpolation" should {
    "extract a variable from FakeRequest url" in {
      FakeRequest("GET", "http://localhost/stores/123") match {
        case url"http://localhost/stores/$id" => id must beEqualTo(id)
      }
    }

    "extract multiple variables from FakeRequest url" in {
      FakeRequest("GET", "http://localhost/stores/123/orders/234/invoices") match {
        case url"http://localhost/stores/$storeId/orders/$orderId/invoices" =>
          storeId must beEqualTo("123")
          orderId must beEqualTo("234")
      }
    }
  }
}
