import sbt._

// scalastyle:off
object Dependencies {
  import Versions._

  // Libraries
  val scalaTest                = "org.scalatest"         %% "scalatest"                     % scalaTestVersion
  val scalaMock                = "org.scalamock"         %% "scalamock"                     % scalaMockVersion
  val akkaTestkit              = "com.typesafe.akka"     %% "akka-testkit"                  % akkaVersion
  val akkaHttpTestkit          = "com.typesafe.akka"     %% "akka-http-testkit"             % akkaHttpVersion
  val akkaMicroservicesCommon  = "ar.lregnier"           %% "akka-microservices-common"     % akkaMicroservicesVersion

  val testkit = Seq(scalaTest, scalaMock, akkaTestkit, akkaHttpTestkit, akkaMicroservicesCommon)
}
