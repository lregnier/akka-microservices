import Settings._

lazy val `akka-microservices-testkit` =
  project
    .in(file("."))
    .settings(commonSettings)
    .settings(libraryDependencies ++= Dependencies.testkit)