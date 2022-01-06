package travel

import spray.json.{DefaultJsonProtocol, RootJsonFormat}

case class TravelProduct (id: String, name: String)

object TravelProduct extends DefaultJsonProtocol {
  implicit val travelProductFormat: RootJsonFormat[TravelProduct] = jsonFormat2(TravelProduct.apply)
}
