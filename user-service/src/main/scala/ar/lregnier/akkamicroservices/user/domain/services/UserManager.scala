package ar.lregnier.akkamicroservices.user.domain.services

import akka.actor.Props
import ar.lregnier.akkamicroservices.common.domain.model.Id
import ar.lregnier.akkamicroservices.common.domain.services._
import ar.lregnier.akkamicroservices.user.domain.model.User
import ar.lregnier.akkamicroservices.user.domain.persistence.UserRepository
import ar.lregnier.akkamicroservices.user.domain.services.UserManager.CreateUpdateUserPayload

import scala.concurrent.Future

object UserManager {
  val Name = "user-manager"

  def props(userRepository: UserRepository): Props =
    Props(new UserManager(userRepository))

  // Messages
  case class CreateUpdateUserPayload(firstName: String, lastName: String)
      extends CreateEntityPayload
      with UpdateEntityPayload
}

class UserManager(userRepository: UserRepository) extends Service {

  def receive: Receive = {
    case CreateEntity(CreateUpdateUserPayload(firstName, lastName)) =>
      val result = createUser(firstName, lastName)
      result pipeTo sender()

    case UpdateEntity(id: Id, CreateUpdateUserPayload(firstName, lastName)) =>
      val result = updateUser(id, firstName, lastName)
      result pipeTo sender()

    case DeleteEntity(id) =>
      val result = deleteUser(id)
      result pipeTo sender()
  }

  def createUser(firstName: String, lastName: String): Future[User] = {
    val result: Future[User] =
      for {
        id <- userRepository.nextId()
        user <- userRepository.save(User(id, firstName, lastName))
      } yield user

    result
  }

  def updateUser(
      id: Id,
      firstName: String,
      lastName: String
  ): Future[Option[User]] = {

    def retrieveUser(): Future[Option[User]] =
      userRepository.find(id)

    def update(user: User): Future[User] = {
      val updatedUser = user.copy(firstName = firstName, lastName = lastName)
      userRepository.save(updatedUser)
    }

    val result =
      retrieveUser() flatMap {
        case Some(user) => update(user).map(Some(_))
        case None       => Future.successful(None)
      }

    result
  }

  def deleteUser(id: Id): Future[Option[User]] = {
    val result = userRepository.remove(id)
    result
  }

}
