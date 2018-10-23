package ar.lregnier.akkamicroservices.common.domain.persistence

import ar.lregnier.akkamicroservices.common.domain.model.{Entity, Id}

import scala.concurrent.Future

/**
  * Base trait for building Repositories.
  *
  * It is located within the domain module following the Dependency Inversion
  * principle in order not to to leak any infrastructure implementation concerns
  * into the domain layer.
  */
trait Repository[A <: Entity] extends IdentityFactory {
  def save(entity: A): Future[A]
  def remove(id: Id): Future[Option[A]]
  def find(id: Id): Future[Option[A]]
  def findAll(): Future[Seq[A]]
}

trait IdentityFactory {
  def nextId(): Future[Id]
}
