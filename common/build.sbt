import Settings._

lazy val `akka-microservices-common` =
  project
    .in(file("."))
    .settings(commonSettings)
    .settings(libraryDependencies ++= Dependencies.common)