package ar.lregnier.akkamicroservices.user.domain.services

import java.util.UUID

import akka.actor.Props
import ar.lregnier.akkamicroservices.common.domain.model.Id
import ar.lregnier.akkamicroservices.common.domain.services.{ListEntities, RetrieveEntity, Service}
import ar.lregnier.akkamicroservices.user.domain.model.User

import scala.concurrent.Future

object UserLookup {
  val Name = "user-lookup"

  def props(): Props =
    Props(new UserLookup)
}

class UserLookup extends Service {

  def receive: Receive = {
    case RetrieveEntity(userId) => {
      def retrieve(userId: Id): Future[Option[User]] =
        Future.successful(Some(User(userId, "firstName", "lastName")))

      retrieve(userId) pipeTo sender
    }

    case ListEntities => {
      def list(): Future[Seq[User]] =
        Future.successful(List((User(Id(UUID.randomUUID().toString), "firstName", "lastName"))))

      list pipeTo sender
    }
  }
}
