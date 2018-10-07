package ar.lregnier.akkamicroservices.testkit.infrastructure.rest

import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.testkit.ScalatestRouteTest
import ar.lregnier.akkamicroservices.common.infrastructure.rest.Json4sJacksonSupport
import org.scalatest.{Matchers, WordSpecLike}

trait RestEndpointSpec
    extends WordSpecLike
    with Matchers
    with ScalatestRouteTest { self =>

  trait RestEndpointScope extends Json4sJacksonSupport {
    val routes: Route

    implicit class ExtendedHttpRequest(request: HttpRequest) {

      def run(): RouteTestResult = request ~> routes ~> runRoute

      def runSeal(): RouteTestResult = request ~> Route.seal(routes) ~> runRoute

    }

    implicit class ExtendedTestResult(result: RouteTestResult) {

      def check[T](body: => T): T = self.check(body)(result)

    }
  }
}
