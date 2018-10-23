import Configs.commonConfigs
import Settings._

lazy val `akka-microservices-user-service` =
  project
    .in(file("."))
    .configs(commonConfigs: _*)
    .settings(settings)
    .settings(libraryDependencies ++= Dependencies.userService)
    .enablePlugins(DockerPlugin, JavaServerAppPackaging)
