package ar.lregnier.akkamicroservices.common.infrastructure.rest

import akka.http.scaladsl.model.StatusCode
import akka.http.scaladsl.server.{Directives, StandardRoute}
import ar.lregnier.akkamicroservices.common.domain.model.DomainObject
import ar.lregnier.akkamicroservices.common.infrastructure.Translator

/**
  * Adds common respond methods which prevent leaking Domain Objects
  * into the HTTP responses. It enforces the use of Representations instead.
  */
trait ResponseHandling {
  self: Directives with Json4sJacksonSupport =>

  def respond(code: StatusCode): StandardRoute =
    complete(code)

  def respond[D <: DomainObject, R <: Representation](value: D)(implicit translator: Translator[D, R]): StandardRoute =
    complete(translator.translate(value))

  def respond[D <: DomainObject, R <: Representation](code: StatusCode, value: D)(
      implicit translator: Translator[D, R]
  ): StandardRoute =
    complete(code, translator.translate(value))

  def respond[D <: DomainObject, R <: Representation](
      values: Seq[D]
  )(implicit translator: Translator[D, R]): StandardRoute =
    complete(values map translator.translate)
}
