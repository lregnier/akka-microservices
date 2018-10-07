package ar.lregnier.akkamicroservices.user.domain.services

import java.util.UUID

import akka.actor.Props
import ar.lregnier.akkamicroservices.common.domain.model.Id
import ar.lregnier.akkamicroservices.common.domain.services._
import ar.lregnier.akkamicroservices.user.domain.model.User
import ar.lregnier.akkamicroservices.user.domain.services.UserManager.CreateUpdateUserPayload

import scala.concurrent.Future

object UserManager {
  val Name = "user-manager"

  def props(): Props =
    Props(new UserManager())

  // Messages
  case class CreateUpdateUserPayload(firstName: String, lastName: String)
      extends CreateEntityPayload
      with UpdateEntityPayload
}

class UserManager extends Service {

  def receive: Receive = {
    case CreateEntity(user: CreateUpdateUserPayload) => {
      def create(user: CreateUpdateUserPayload): Future[User] =
        Future.successful(User(Id(UUID.randomUUID().toString), user.firstName, user.lastName))

      create(user) pipeTo sender
    }

    case UpdateEntity(userId: Id, user: CreateUpdateUserPayload) => {
      def update(id: Id, user: CreateUpdateUserPayload): Future[Option[User]] =
        Future.successful(Some(User(id, user.firstName, user.lastName)))

      update(userId, user) pipeTo sender
    }

    case DeleteEntity(userId) => {
      def delete(userId: Id): Future[Option[User]] =
        Future.successful(Some(User(userId, "firstName", "lastName")))

      delete(userId) pipeTo sender
    }
  }
}
