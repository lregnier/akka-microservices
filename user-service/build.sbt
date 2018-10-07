import Settings._

lazy val `akka-microservices-user-service` =
  project
    .in(file("."))
    .settings(commonSettings)
    .settings(libraryDependencies ++= Dependencies.userService)