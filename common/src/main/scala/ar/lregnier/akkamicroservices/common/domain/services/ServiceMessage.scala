package ar.lregnier.akkamicroservices.common.domain.services

import ar.lregnier.akkamicroservices.common.domain.model.Id

/**
  * Marker trait for Service Messages
  */
trait ServiceMessage

/**
  * Marker trait
  */
trait CreateEntityPayload

/**
  * Marker trait
  */
trait UpdateEntityPayload

case class CreateEntity(entityData: CreateEntityPayload) extends ServiceMessage
case class RetrieveEntity(entityId: Id) extends ServiceMessage
case class UpdateEntity(entityId: Id, entityData: UpdateEntityPayload) extends ServiceMessage
case class DeleteEntity(entityId: Id) extends ServiceMessage
case object ListEntities extends ServiceMessage
