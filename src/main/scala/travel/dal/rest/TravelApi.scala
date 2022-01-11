package travel.dal.rest

import akka.actor.ActorSystem
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.unmarshalling.Unmarshal
import sangria.macros.LiteralGraphQLStringContext
import spray.json.DefaultJsonProtocol._
import spray.json.enrichAny
import travel.dal.model.{GetProductsVars, TravelProduct}
import travel.util.{GraphQLRequestBuilder, HttpClient}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

class TravelApi(uri: String)(implicit
    actorSystem: ActorSystem,
    executionContext: ExecutionContext
) extends HttpClient {

  def getProducts(
      minPriceEur: Double,
      maxPriceEur: Double
  ): Future[Option[List[TravelProduct]]] = for {
    response <- send(
      build(uri, TravelApi.Queries.getProducts(minPriceEur, maxPriceEur))
    )
    payload <- Unmarshal(response.entity)
      .to[Map[String, Map[String, Option[List[TravelProduct]]]]]
    parsed <- Try(payload("data")("products")) match {
      case Success(value) => Future(value)
      case Failure(exception) =>
        actorSystem.log.error(
          s"failed to parse the payload: ${exception.getMessage}"
        )
        Future.successful(None)
    }
  } yield parsed
}

object TravelApi {

  object Queries {
    def getProducts(minPriceEur: Double, maxPriceEur: Double): String = {
      val vars = Some(GetProductsVars(minPriceEur, maxPriceEur))
      val query =
        graphql"""
          query getProducts($$minPriceEur: numeric!, $$maxPriceEur: numeric!) {
	        products( where: {
			  _and: [
				{ product_prices: { price_eur: { _gte: $$minPriceEur } } }
				{ product_prices: { price_eur: { _lte: $$maxPriceEur } } }
			  ]
		    }
	      ) {
		  id
		  name
		  type
		  product_prices {
			price_eur
			flight_included
			footer
			header
		  }
	    }
      }""".source.get

      GraphQLRequestBuilder.build(query, vars, _ => vars.map(_.toJson))
    }
  }

}
