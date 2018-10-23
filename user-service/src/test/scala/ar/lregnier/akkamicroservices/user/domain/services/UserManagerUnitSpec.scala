package ar.lregnier.akkamicroservices.user.domain.services

import java.util.UUID

import ar.lregnier.akkamicroservices.common.domain.model.Id
import ar.lregnier.akkamicroservices.common.domain.services._
import ar.lregnier.akkamicroservices.testkit.domain.services.ServiceSpec
import ar.lregnier.akkamicroservices.user.domain.model.User
import ar.lregnier.akkamicroservices.user.domain.persistence.UserRepository
import ar.lregnier.akkamicroservices.user.domain.services.UserManager.CreateUpdateUserPayload

import scala.concurrent.Future

// scalastyle:off
class UserManagerUnitSpec extends ServiceSpec {
  // Fixtures
  val userId = Id.fromUUID(UUID.randomUUID())
  val nonExistentUserId = Id.fromUUID(UUID.randomUUID())
  val firstName = "John"
  val lastName = "Doe"
  val user = User(userId, firstName, lastName)

  trait Scope extends ServiceScope {
    val userRepository = mock[UserRepository]
    val service = system.actorOf(UserManager.props(userRepository))
  }

  "When receiving a CreateEntity msg, it" should {
    "create a new Entity if no errors and return it back" in new Scope {
      // Set expectations
      (userRepository.nextId _).expects().returning(Future.successful(userId))
      (userRepository.save _).expects(user).returning(Future.successful(user))

      // Send msg
      service ! CreateEntity(CreateUpdateUserPayload(firstName, lastName))

      // Verify
      expectMsg(user)
    }
    "return a failure msg if there's an error while asking for the Entity's Id" in new Scope {
      // Set expectations
      val unexpectedException = new Exception("BOOM!")
      (userRepository.nextId _).expects().returning(Future.failed(unexpectedException))

      // Send msg
      service ! CreateEntity(CreateUpdateUserPayload(firstName, lastName))

      // Verify
      expectMsgFailure(unexpectedException)
    }
    "return a failure msg if there's an error while saving the Entity" in new Scope {
      // Set expectations
      val unexpectedException = new Exception("BOOM!")
      (userRepository.nextId _).expects().returning(Future.successful(userId))
      (userRepository.save _).expects(user).returning(Future.failed(unexpectedException))

      // Send msg
      service ! CreateEntity(CreateUpdateUserPayload(firstName, lastName))

      // Verify
      expectMsgFailure(unexpectedException)
    }
  }

  "When receiving an UpdateEntity msg, it" should {
    "update the requested Entity if no errors and return it back" in new Scope {
      val updatedFirstName = "Richard"
      val updatedLastName = "Roe"
      val updatedUser = user.copy(firstName = updatedFirstName, lastName = updatedLastName)

      // Set expectations
      (userRepository.find _).expects(userId).returning(Future.successful(Some(user)))
      (userRepository.save _).expects(updatedUser).returning(Future.successful(updatedUser))

      // Send msg
      service ! UpdateEntity(userId, CreateUpdateUserPayload(updatedFirstName, updatedLastName))

      // Verify
      expectMsg(Some(updatedUser))
    }
    "return None if the requested Entity cannot be found" in new Scope {
      val updatedFirstName = "Richard"
      val updatedLastName = "Roe"

      // Set expectations
      (userRepository.find _).expects(nonExistentUserId).returning(Future.successful(None))

      // Send msg
      service ! UpdateEntity(nonExistentUserId, CreateUpdateUserPayload(updatedFirstName, updatedLastName))

      // Verify
      expectMsg(None)
    }
    "return a failure msg if there's an error while finding the Entity" in new Scope {
      val updatedFirstName = "Richard"
      val updatedLastName = "Roe"

      // Set expectations
      val unexpectedException = new Exception("BOOM!")
      (userRepository.find _).expects(userId).returning(Future.failed(unexpectedException))

      // Send msg
      service ! UpdateEntity(userId, CreateUpdateUserPayload(updatedFirstName, updatedLastName))

      // Verify
      expectMsgFailure(unexpectedException)
    }
    "return a failure msg if there's an error while saving the Entity" in new Scope {
      val updatedFirstName = "Richard"
      val updatedLastName = "Roe"
      val updatedUser = user.copy(firstName = updatedFirstName, lastName = updatedLastName)

      // Set expectations
      val unexpectedException = new Exception("BOOM!")
      (userRepository.find _).expects(userId).returning(Future.successful(Some(user)))
      (userRepository.save _).expects(updatedUser).returning(Future.failed(unexpectedException))

      // Send msg
      service ! UpdateEntity(userId, CreateUpdateUserPayload(updatedFirstName, updatedLastName))

      // Verify
      expectMsgFailure(unexpectedException)
    }
  }

  "When receiving an DeleteEntity msg, it" should {
    "delete the requested Entity if no errors and return it back" in new Scope {
      // Set expectations
      (userRepository.remove _).expects(userId).returning(Future.successful(Some(user)))

      // Send msg
      service ! DeleteEntity(userId)

      // Verify
      expectMsg(Some(user))
    }
    "return None if the requested Entity cannot be found" in new Scope {
      // Set expectations
      (userRepository.remove _).expects(userId).returning(Future.successful(None))

      // Send msg
      service ! DeleteEntity(userId)

      // Verify
      expectMsg(None)
    }
    "return a failure msg if there's an error while deleting the Entity" in new Scope {
      // Set expectations
      val unexpectedException = new Exception("BOOM!")
      (userRepository.remove _).expects(userId).returning(Future.failed(unexpectedException))

      // Send msg
      service ! DeleteEntity(userId)

      // Verify
      expectMsgFailure(unexpectedException)
    }
  }

}
