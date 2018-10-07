import sbt._

// scalastyle:off
object Dependencies {
  import Versions._

  // Libraries
  object Compile {
    val akkaActor                  = "com.typesafe.akka"               %% "akka-actor"                         % akkaVersion
    val akkaCluster                = "com.typesafe.akka"               %% "akka-cluster"                       % akkaVersion
    val akkaManagement             = "com.lightbend.akka.management"   %% "akka-management"                    % akkaManagementVersion
    val akkaManagementClusterHttp  = "com.lightbend.akka.management"   %% "akka-management-cluster-http"       % akkaManagementVersion
    val akkaClusterBootstrap       = "com.lightbend.akka.management"   %% "akka-management-cluster-bootstrap"  % akkaManagementVersion
    val akkaDiscoveryDns           = "com.lightbend.akka.discovery"    %% "akka-discovery-dns"                 % akkaManagementVersion
    val simpleAkkaDowning          = "com.ajjpj.simple-akka-downing"   %% "simple-akka-downing"                % simpleAkkaDowningVersion
    val akkaHttp                   = "com.typesafe.akka"               %% "akka-http"                          % akkaHttpVersion
    val akkaHttpJson4s             = "de.heikoseeberger"               %% "akka-http-json4s"                   % akkaHttpJson4sVersion
    val json4s                     = "org.json4s"                      %% "json4s-jackson"                     % json4sVersion
    val json4sExt                  = "org.json4s"                      %% "json4s-ext"                         % json4sVersion
  }

  val akka = Seq(Compile.akkaActor)
  val akkaCluster = Seq(Compile.akkaCluster, Compile.simpleAkkaDowning)
  val akkaManagement = Seq(Compile.akkaManagement, Compile.akkaManagementClusterHttp, Compile.akkaClusterBootstrap, Compile.akkaDiscoveryDns)
  val akkaHttp = Seq(Compile.akkaHttp, Compile.akkaHttpJson4s, Compile.json4s, Compile.json4sExt)

  val common = akka ++ akkaCluster ++ akkaManagement ++ akkaHttp
}
