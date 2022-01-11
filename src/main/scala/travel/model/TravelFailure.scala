package travel.model

trait TravelFailure

case class GetProductsFailure(
    message: String = "unexpected error occurred while retrieving products"
) extends TravelFailure
