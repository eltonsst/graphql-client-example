package travel

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import spray.json.enrichAny
import travel.dal.rest.TravelApi
import travel.model.GetProductsFailure
import travel.service.TravelService

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

class Routes()(implicit
    actorSystem: ActorSystem,
    executionContext: ExecutionContext
) {

  private val travelApi = new TravelApi(Config.GraphqlUrl)
  protected val travelService = new TravelService(travelApi)

  private lazy val versionRoute = {
    path("version") {
      get {
        complete(BuildInfo.toString)
      }
    }
  }

  private lazy val productsRoute = {
    path("products") {
      parameters("minPriceEur".as[Double], "maxPriceEur".as[Double]) {
        (min, max) =>
          get {
            travelService.getProducts(min, max).transformWith {
              // pattern matching to handle specific errors
              // add case classes for each type of failure
              case Failure(exception) =>
                actorSystem.log.error(
                  s"something went wrong, error: ${exception.getMessage}"
                )
                Future.successful(GetProductsFailure)
              case Success(value) => Future.successful(value)
            }

            val futRoute = for {
              data <- travelService.getProducts(min, max)
            } yield complete(data.toJson.toString())

            onComplete(futRoute)(route => route.get)
          }
      }
    }
  }

  lazy val routes: Route = {
    versionRoute ~ productsRoute
  }
}
