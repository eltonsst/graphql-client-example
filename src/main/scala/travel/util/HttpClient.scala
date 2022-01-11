package travel.util

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._

import scala.concurrent.Future

trait HttpClient {
  def build(uri: String, body: String): HttpRequest =
    HttpRequest(
      method = HttpMethods.POST,
      uri = uri,
      headers = Nil,
      entity = HttpEntity(ContentTypes.`application/json`, body),
      protocol = HttpProtocols.`HTTP/1.1`
    )

  def send(httpRequest: HttpRequest)(implicit
      ac: ActorSystem
  ): Future[HttpResponse] =
    Http().singleRequest(httpRequest)
}
