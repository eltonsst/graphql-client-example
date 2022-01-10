package travel

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import com.typesafe.scalalogging.LazyLogging

import scala.util.Failure
import scala.util.Success
import scala.concurrent.ExecutionContext

object App {

  private def startHttpServer(
      routes: Route
  )(implicit system: ActorSystem, executionContext: ExecutionContext): Unit = {
    val futureBinding = Http().newServerAt("127.0.0.1", Config.port).bind(routes)
    futureBinding.onComplete {
      case Success(binding) =>
        val address = binding.localAddress
        system.log.info(
          "Server online at http://{}:{}/",
          address.getHostString,
          address.getPort
        )
      case Failure(ex) =>
        system.log.error("Failed to bind HTTP endpoint, terminating system", ex)
        system.terminate()
    }
  }

  def main(args: Array[String]): Unit = {
    implicit val system: ActorSystem = ActorSystem("system")
    implicit val executionContext: ExecutionContext = system.dispatcher

    system.log.info(s"load configuration: ${Config.toString}")

    val routes = new Routes()
    startHttpServer(routes.routes)
  }
}
