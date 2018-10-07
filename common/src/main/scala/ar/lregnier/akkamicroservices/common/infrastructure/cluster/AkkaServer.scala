package ar.lregnier.akkamicroservices.common.infrastructure.cluster

import akka.actor.{ActorSystem, Extension}
import akka.management.AkkaManagement
import akka.management.cluster.bootstrap.ClusterBootstrap
import com.typesafe.config.{Config, ConfigFactory}

import scala.util.{Failure, Success}

object AkkaServer {
  def start(actorSystemName: String): ActorSystem = {
    // 1. Start Actor System
    val system = ActorSystem(actorSystemName)
    implicit val ec = system.dispatcher
    val log = system.log

    // 2. Start Akka Management extension
    val uri = AkkaManagement(system).start()
    uri onComplete {
      case Success(uri) => log.info("[Akka Management] successfully started on [{}]", uri)
      case Failure(e)   => log.error(e, "[Akka Management] failed to start.")
    }

    // 3. Start Cluster Bootstrap extension
    ClusterBootstrap(system).start()

    system
  }

  def start(): ActorSystem = {
    val actorSystemName = new AkkaServerSettings(ConfigFactory.load()).actorSystemName
    start(actorSystemName)
  }
}

// Settings
class AkkaServerSettings(config: Config) extends Extension {
  private val akkaConfig = config.getConfig("akka")
  val actorSystemName = akkaConfig.getString("actor-system-name")
}
