package ar.lregnier.akkamicroservices.user.domain.services

import akka.actor.Props
import ar.lregnier.akkamicroservices.common.domain.services.{ListEntities, RetrieveEntity, Service}
import ar.lregnier.akkamicroservices.user.domain.persistence.UserRepository

object UserLookup {
  val Name = "user-lookup"

  def props(userRepository: UserRepository): Props =
    Props(new UserLookup(userRepository: UserRepository))
}

class UserLookup(userRepository: UserRepository) extends Service {

  def receive: Receive = {
    case RetrieveEntity(userId) =>
      val result = userRepository.find(userId)
      result pipeTo sender

    case ListEntities =>
      val result = userRepository.findAll()
      result pipeTo sender

  }
}
