package travel

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model._
import akka.http.scaladsl.unmarshalling.Unmarshal
import sangria.macros.LiteralGraphQLStringContext
import spray.json.DefaultJsonProtocol._
import spray.json.enrichAny

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

class TravelApi(uri: String)(implicit
    actorSystem: ActorSystem,
    executionContext: ExecutionContext
) {

  def getProducts(
      minPriceEur: Double,
      maxPriceEur: Double
  ): Future[Option[List[TravelProduct]]] = {
    val sendRequest = Http().singleRequest(request =
      HttpRequest(
        method = HttpMethods.POST,
        uri = uri,
        entity = HttpEntity(
          ContentTypes.`application/json`,
          TravelApi.Queries.getProducts(minPriceEur, maxPriceEur)
        ),
        protocol = HttpProtocols.`HTTP/1.1`
      )
    )

    val x = for {
      response <- sendRequest
      payload <- Unmarshal(response.entity)
        .to[Map[String, Map[String, Option[List[TravelProduct]]]]]
      parsed <- Try(payload("data")("products")) match {
        case Success(value)     => Future(value)
        case Failure(exception) => Future.failed(exception)
      }
    } yield parsed

    x
  }

}

object TravelApi {

  object Queries {
    def getProducts(minPriceEur: Double, maxPriceEur: Double): String = {
      val vars = Some(GetProductsVars(minPriceEur, maxPriceEur))
      val query =
        graphql"""
            query getProducts($$minPriceEur: numeric!, $$maxPriceEur: numeric!){
              products(where: {
                product_prices: {
                  price_eur: {_gte: $$minPriceEur}
                }
            }) {
              id
              name
            }
          }
      """.source.get

      GraphQLRequestBuilder.build(query, vars, _ => vars.map(_.toJson))
    }
  }

}
