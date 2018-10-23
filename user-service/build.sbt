import Settings._

lazy val `akka-microservices-user-service` =
  project
    .in(file("."))
    .settings(settings)
    .settings(libraryDependencies ++= Dependencies.userService)
    .enablePlugins(DockerPlugin, JavaServerAppPackaging)
