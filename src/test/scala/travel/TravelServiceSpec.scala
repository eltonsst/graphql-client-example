package travel

import akka.actor.ActorSystem
import akka.stream.Materializer
import org.mockito.Mockito.when
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.mockito.MockitoSugar.mock
import travel.TravelServiceSpec.{out, products}
import travel.dal.model.{ProductPrice, TravelProduct}
import travel.dal.rest.TravelApi
import travel.model.{ProductPriceView, TravelProductView}
import travel.service.TravelService

import scala.concurrent.{ExecutionContext, Future}

class TravelServiceSpec extends AnyWordSpec with Matchers with ScalaFutures {
  implicit val system: ActorSystem = ActorSystem(suiteName)
  implicit val materializer: Materializer = Materializer(system)
  implicit val executionContext: ExecutionContext = system.dispatcher

  val travelApi: TravelApi = mock[TravelApi]
  val travelService = new TravelService(travelApi)

  "TravelService" should {
    "retrieve products correctly" in {
      when(travelApi.getProducts(1,2)).thenReturn(Future.successful(Some(products)))

      whenReady(travelService.getProducts(1,2)) { response =>
        response shouldBe out
      }
    }

    "map to empty list" in {
      when(travelApi.getProducts(1,2)).thenReturn(Future.successful(None))

      whenReady(travelService.getProducts(1,2)) { response =>
        response shouldBe List()
      }
    }
  }
}

object TravelServiceSpec {
  val products: List[TravelProduct] =
    TravelProduct("", "", "", ProductPrice(1.4, false, None, "") :: ProductPrice(1.2, false, None, "") :: Nil) :: Nil

  val out: List[TravelProductView] =
    TravelProductView("", "", "", ProductPriceView(1.4, false, None, "")) ::
      TravelProductView("", "", "", ProductPriceView(1.2, false, None, "")) :: Nil

}