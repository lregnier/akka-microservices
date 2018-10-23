import sbt.Keys._
import sbt._

object Testing {
  // Configs
  lazy val IntegrationTest = config("it") extend Test

  // Filters
  def unitFilter(name: String): Boolean = (name endsWith "UnitSpec")
  def itFilter(name: String): Boolean = (name endsWith "ItSpec")

  // Tasks
  lazy val testAll = TaskKey[Unit]("test-all")

  // Settings
  lazy val unitTestSettings = Seq(
    fork in Test := true,
    parallelExecution in Test := false,
    testOptions in Test := Seq(Tests.Filter(unitFilter))
  )

  lazy val itTestSettings =
    inConfig(IntegrationTest)(Defaults.testSettings) ++
      Seq(
        fork in IntegrationTest := true,
        parallelExecution in IntegrationTest := false,
        scalaSource in IntegrationTest := baseDirectory.value / "src/it/scala",
        testOptions in IntegrationTest := Seq(Tests.Filter(itFilter)))

  lazy val settings =
    unitTestSettings ++
    itTestSettings ++
    Seq(testAll := (test in IntegrationTest).dependsOn(test in Test).value)

}
