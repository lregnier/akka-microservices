package ar.lregnier.akkamicroservices.common.infrastructure.rest

import akka.actor.{ActorSystem, CoordinatedShutdown, ExtendedActorSystem, Extension, ExtensionId, ExtensionIdProvider}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import com.typesafe.config.Config

import scala.util.{Failure, Success}

object RestServer {

  private object BindFailure extends CoordinatedShutdown.Reason

  def start(restEndpoints: Seq[RestEndpoint] = Seq.empty)(implicit system: ActorSystem): Unit = {
    import system.dispatcher

    implicit val mat = ActorMaterializer()
    val log = system.log

    val settings = HttpServerSettings(system)
    val (host, port) = (settings.host, settings.port)
    val resourcesRoutes = restEndpoints.map(_.routes).reduce(_ ~ _)
    val routes = DefaultRoutes.buildRoutes(resourcesRoutes)

    val shutdown = CoordinatedShutdown(system)

    Http()
      .bindAndHandle(routes, host, port)
      .onComplete {
        case Failure(error) =>
          log.error(error, "Shutting down, because cannot bind to {}:{}!", host, port)
          shutdown.run(BindFailure)

        case Success(binding) =>
          log.info("Listening for HTTP connections on {}", binding.localAddress)
          shutdown.addTask(CoordinatedShutdown.PhaseServiceUnbind, "http-server.unbind") { () =>
            binding.unbind()
          }
      }
  }

}

object DefaultRoutes {
  private def defaultRoutes: Route =
    (path("health-check") & get) {
      complete(StatusCodes.OK)
    }

  def buildRoutes(apiRoutes: Route): Route =
    defaultRoutes ~
      path("api") {
        apiRoutes
      }
}

// Settings
class HttpServerSettings(config: Config) extends Extension {
  private val httpServerConfig = config.getConfig("http")
  val host = httpServerConfig.getString("host")
  val port = httpServerConfig.getInt("port")
}

object HttpServerSettings extends ExtensionId[HttpServerSettings] with ExtensionIdProvider {
  override def createExtension(system: ExtendedActorSystem): HttpServerSettings =
    new HttpServerSettings(system.settings.config)

  override def lookup(): ExtensionId[_ <: Extension] = HttpServerSettings

}
