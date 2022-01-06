package travel

import org.scalatest.concurrent.ScalaFutures
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class RoutesSpec extends AnyWordSpec with Matchers with ScalaFutures {
  "spec" should {
    "be true" in {
      1 shouldBe 1
    }
  }
}
