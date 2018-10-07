package ar.lregnier.akkamicroservices.common.utils

import akka.pattern.AskSupport
import akka.util.Timeout
import com.typesafe.config.ConfigFactory
import ar.lregnier.akkamicroservices.common.utils.DurationExtensions._

trait DefaultAskSupport extends AskSupport {
  implicit val timeout: Timeout = {
    val config = ConfigFactory.load()
    Timeout(config.getDuration("akka.default-ask-timeout").toScala)
  }
}
