package travel.service

import travel.dal.model.TravelProduct
import travel.dal.rest.TravelApi
import travel.model.{ProductPriceView, TravelProductView}
import travel.service.TravelService.flatten

import scala.concurrent.{ExecutionContext, Future}

class TravelService(travelApi: TravelApi)(implicit
    executionContext: ExecutionContext
) {
  def getProducts(
      minPriceEur: Double,
      maxPriceEur: Double
  ): Future[List[TravelProductView]] = {
    for {
      maybeProducts <- travelApi.getProducts(minPriceEur, maxPriceEur)
      productsView = maybeProducts match {
        case Some(products) => products.flatMap(flatten)
        case None           => List.empty[TravelProductView]
      }
    } yield productsView
  }
}

object TravelService {
  private def flatten(t: TravelProduct): Seq[TravelProductView] = {
    t.product_prices.map(p =>
      TravelProductView(
        id = t.id,
        name = t.name,
        `type` = t.`type`,
        price = ProductPriceView(
          priceEur = p.price_eur,
          flightIncluded = p.flight_included,
          footer = p.footer,
          header = p.header
        )
      )
    )

  }
}
