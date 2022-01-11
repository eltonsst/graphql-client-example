package travel.dal.model

import spray.json.{DefaultJsonProtocol, RootJsonFormat}

case class ProductPrice(
    price_eur: Double,
    flight_included: Boolean,
    footer: Option[String],
    header: String
)

case class TravelProduct(
    id: String,
    name: String,
    `type`: String,
    product_prices: Seq[ProductPrice]
)

object TravelProduct extends DefaultJsonProtocol {
  implicit val productPriceFormat: RootJsonFormat[ProductPrice] = jsonFormat4(
    ProductPrice.apply
  )

  implicit val travelProductFormat: RootJsonFormat[TravelProduct] = jsonFormat4(
    TravelProduct.apply
  )
}
