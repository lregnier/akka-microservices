package ar.lregnier.akkamicroservices.common.infrastructure.persistence

import java.util.UUID

import ar.lregnier.akkamicroservices.common.domain.model.{Entity, Id}
import ar.lregnier.akkamicroservices.common.domain.persistence.Repository

import scala.collection.concurrent
import scala.concurrent.Future

/**
  * Repository backed by an in-memory concurrent Map
  * @tparam A Repository's entity type
  */
trait InMemoryRepository[A <: Entity] { self: Repository[A] =>
  protected val store: concurrent.Map[Id, A] = new concurrent.TrieMap[Id, A]()

  override def save(entity: A): Future[A] = Future.successful {
    store += (entity.id -> entity)
    entity
  }

  override def remove(id: Id): Future[Option[A]] = Future.successful {
    store.remove(id)
  }

  override def find(id: Id): Future[Option[A]] = Future.successful {
    store.get(id)
  }

  override def findAll(): Future[Seq[A]] = Future.successful {
    store.toSeq.map {
      case (_, value) => value
    }
  }

  override def nextId(): Future[Id] = Future.successful(Id(UUID.randomUUID().toString))

}
