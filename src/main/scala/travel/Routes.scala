package travel

import akka.actor.ActorSystem
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import spray.json.enrichAny

import scala.concurrent.ExecutionContext

class Routes()(implicit actorSystem: ActorSystem, executionContext: ExecutionContext) {

  private val travelApi = new TravelApi(Config.GraphqlUrl)

  private lazy val versionRoute = {
    path("version") {
      get {
        complete(BuildInfo.toString)
      }
    }
  }

  private lazy val productsRoute = {
    path("products") {
      parameters("minPriceEur".as[Double], "maxPriceEur".as[Double]) { (min, max) =>
        get {
          val futRoute = for {
            data <- travelApi.getProducts(min, max)
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
