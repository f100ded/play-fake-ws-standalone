package org.f100ded.play.fakews

class RequestMethodExtractor(method: String) {
  def unapply(request: FakeRequest): Option[FakeRequest] = Some(request).filter(_.method.equalsIgnoreCase(method))
}
