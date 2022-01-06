package travel

import spray.json.{DefaultJsonProtocol, RootJsonFormat}

sealed trait Vars

case class GetProductsVars (minPriceEur: Double, maxPriceEur: Double) extends Vars

object Vars extends  DefaultJsonProtocol {
  implicit val getProductsVarsFormat: RootJsonFormat[GetProductsVars] = jsonFormat2(GetProductsVars.apply)
}