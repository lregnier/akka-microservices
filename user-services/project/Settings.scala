import org.scalafmt.sbt.ScalafmtPlugin.autoImport._
import org.scalastyle.sbt.ScalastylePlugin.autoImport._
import sbt.Keys._

// scalastyle:off
object Settings {
  lazy val commonScalacOptions = Seq(
    s"-target:jvm-${Versions.javaVersion}",
    "-encoding", "UTF-8",
    "-unchecked",
    "-deprecation",
    "-language:_",
    "-Ywarn-dead-code",
    "Xlint:unused"
  )

  lazy val commonJavacOptions = Seq(
    "-source", Versions.javaVersion,
    "-target", Versions.javaVersion,
    "-encoding", "UTF-8")

  lazy val commonSettings =
    buildSettings ++
    compilerSettings ++
    scalafmtSettings ++
    scalaStyleSettings

  lazy val buildSettings = Seq(
    organization  := "ar.lregnier",
    version       := "0.1.0-SNAPSHOT",
    scalaVersion  := Versions.scalaVersion
  )

  lazy val compilerSettings = Seq(
    javacOptions  ++= commonJavacOptions,
    scalacOptions ++= commonScalacOptions
  )

  lazy val scalafmtSettings =
    Seq(
      scalafmtOnCompile := true
    )

  lazy val scalaStyleSettings =
    Seq(
      scalastyleFailOnError := true
    )

}
