package ar.lregnier.akkamicroservices.common.utils

import java.time.{Duration => JavaDuration}

import scala.concurrent.duration.{Duration, FiniteDuration}

trait DurationExtensions {

  implicit class JavaDurationExtensions(d: JavaDuration) {
    def toScala: FiniteDuration =
      Duration.fromNanos(d.toNanos)
  }

  implicit class ScalaDurationExtensions(d: FiniteDuration) {
    def toJava: java.time.Duration = JavaDuration.ofNanos(d.toNanos)
  }

}

object DurationExtensions extends DurationExtensions
