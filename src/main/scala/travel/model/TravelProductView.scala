package travel.model

import spray.json.{DefaultJsonProtocol, RootJsonFormat}

case class ProductPriceView(
    priceEur: Double,
    flightIncluded: Boolean,
    footer: Option[String],
    header: String
)

case class TravelProductView(
    id: String,
    name: String,
    `type`: String,
    price: ProductPriceView
)

object TravelProductView extends DefaultJsonProtocol {
  implicit val productPriceFormat: RootJsonFormat[ProductPriceView] =
    jsonFormat4(
      ProductPriceView.apply
    )

  implicit val travelProductFormat: RootJsonFormat[TravelProductView] =
    jsonFormat4(
      TravelProductView.apply
    )
}
