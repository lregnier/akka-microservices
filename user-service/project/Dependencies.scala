import sbt._

// scalastyle:off
object Dependencies {
  import Versions._

  // Libraries
  val akkaMicroservicesCommon   = "ar.lregnier"    %% "akka-microservices-common"     % akkaMicroservicesVersion
  val akkaMicroservicesTestkit  = "ar.lregnier"    %% "akka-microservices-testkit"    % akkaMicroservicesVersion  % Test

  val userService = Seq(akkaMicroservicesCommon, akkaMicroservicesTestkit)
}
