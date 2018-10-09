package ar.lregnier.akkamicroservices.common.infrastructure.rest

import akka.http.scaladsl.server.{Directives, Route}
import ar.lregnier.akkamicroservices.common.utils.DefaultAskSupport
import de.heikoseeberger.akkahttpjson4s.Json4sSupport
import org.json4s.{DefaultFormats, jackson}

trait RestEndpoint extends Directives with Json4sJacksonSupport with DefaultAskSupport with ResponseHandling {

  def routes: Route

}

trait Json4sJacksonSupport extends Json4sSupport {
  implicit val serialization = jackson.Serialization
  implicit val formats = DefaultFormats
}
