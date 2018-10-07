package ar.lregnier.akkamicroservices.common.domain.model

import java.util.UUID

import scala.util.Try

/**
  * Marker trait for Domain Objects
  */
trait DomainObject

/**
  * Default Identity implementation
  * @param value must be a valid UUID string
  */
case class Id(value: String) extends DomainObject {
  require(Try(UUID.fromString(value)).isSuccess, "invalid-id-format")

  override def toString(): String = value
}

/**
  * Base trait for building Entities
  */
trait Entity extends DomainObject {
  val id: Id
}
