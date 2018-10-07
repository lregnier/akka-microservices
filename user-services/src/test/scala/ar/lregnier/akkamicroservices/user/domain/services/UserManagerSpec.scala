package ar.lregnier.akkamicroservices.user.domain.services

import java.util.UUID

import akka.testkit.TestActorRef
import ar.lregnier.akkamicroservices.common.domain.model.Id
import ar.lregnier.akkamicroservices.common.domain.services.{CreateEntity, DeleteEntity, UpdateEntity}
import ar.lregnier.akkamicroservices.testkit.domain.services.ServiceSpec
import ar.lregnier.akkamicroservices.user.domain.model.User
import ar.lregnier.akkamicroservices.user.domain.services.UserManager.CreateUpdateUserPayload

class UserManagerSpec extends ServiceSpec {

  // Fixtures
  val userId = Id(UUID.randomUUID().toString)
  val nonExistentUserId = Id(UUID.randomUUID().toString)
  val firstName = "First name"
  val lastName = "Last name"
  val userEntity = User(userId, firstName, lastName)

  trait Scope extends ServiceScope {
    val service = TestActorRef(new UserManager())
  }

  "When sending a CreateEntity msg, the service" should {
    "respond with the created User if no errors" in new Scope {
      val firstName = "Create name"
      val lastName = "Create description"

      service ! CreateEntity(CreateUpdateUserPayload(firstName, lastName))

      expectMsgPF() {
        case User(_, createdFirstName, createdLastName) =>
          createdFirstName shouldEqual firstName
          createdLastName shouldEqual lastName
      }
    }
  }

  "When sending an UpdateEntity msg, the service" should {
    "respond with the updated User if it exists" in new Scope {
      val id = userId
      val firstName = "Update first name"
      val lastName = "Update last name"

      service ! UpdateEntity(id, CreateUpdateUserPayload(firstName, lastName))

      expectMsg(Some(User(id, firstName, lastName)))
    }
  }

  "When sending a DeleteEntity msg, the service" should {
    "respond with the deleted User if it exists" in new Scope {
      val id = userId

      service ! DeleteEntity(id)

      expectMsgPF() {
        case Some(User(deletedId, _, _)) =>
          deletedId shouldEqual id
      }
    }
  }

}
