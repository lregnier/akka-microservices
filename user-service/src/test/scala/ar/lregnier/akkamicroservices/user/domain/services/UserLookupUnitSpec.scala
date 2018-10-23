package ar.lregnier.akkamicroservices.user.domain.services

import java.util.UUID

import ar.lregnier.akkamicroservices.common.domain.model.Id
import ar.lregnier.akkamicroservices.common.domain.services._
import ar.lregnier.akkamicroservices.testkit.domain.services.ServiceSpec
import ar.lregnier.akkamicroservices.user.domain.model.User
import ar.lregnier.akkamicroservices.user.domain.persistence.UserRepository

import scala.concurrent.Future

// scalastyle:off
class UserLookupUnitSpec extends ServiceSpec {
  // Fixtures
  val userId = Id.fromUUID(UUID.randomUUID())
  val nonExistentUserId = Id.fromUUID(UUID.randomUUID())
  val firstName = "John"
  val lastName = "Doe"
  val user = User(userId, firstName, lastName)

  trait Scope extends ServiceScope {
    val userRepository = mock[UserRepository]
    val service = system.actorOf(UserLookup.props(userRepository))
  }

  "When receiving a RetrieveEntity msg, it" should {
    "return the requested Entity if no errors" in new Scope {
      // Set expectations
      (userRepository.find _).expects(userId).returning(Future.successful(Some(user)))

      // Send msg
      service ! RetrieveEntity(userId)

      // Verify
      expectMsg(Some(user))
    }
    "return None if the requested Entity cannot be found" in new Scope {
      // Set expectations
      (userRepository.find _).expects(nonExistentUserId).returning(Future.successful(None))

      // Send msg
      service ! RetrieveEntity(nonExistentUserId)

      // Verify
      expectMsg(None)
    }
    "return a failure msg if there's an error while finding the Entity" in new Scope {
      // Set expectations
      val unexpectedException = new Exception("BOOM!")
      (userRepository.find _).expects(userId).returning(Future.failed(unexpectedException))

      // Send msg
      service ! RetrieveEntity(userId)

      // Verify
      expectMsgFailure(unexpectedException)
    }
  }

  "When receiving an ListEntities msg, it" should {
    "return all existing Entities if no errors" in new Scope {
      // Set expectations
      val entities = Seq(user)
      (userRepository.findAll _).expects().returning(Future.successful(entities))

      // Send msg
      service ! ListEntities

      // Verify
      expectMsg(entities)
    }
    "return a failure msg if there's an error while finding all Entities" in new Scope {
      // Set expectations
      val unexpectedException = new Exception("BOOM!")
      (userRepository.findAll _).expects().returning(Future.failed(unexpectedException))

      // Send msg
      service ! ListEntities

      // Verify
      expectMsgFailure(unexpectedException)
    }
  }

}