package com.mestudent.template.domain.services

import java.util.UUID

import ar.lregnier.akkamicroservices.common.domain.model.Id
import ar.lregnier.akkamicroservices.common.domain.services.{CreateEntity, DeleteEntity, RetrieveEntity, UpdateEntity}
import ar.lregnier.akkamicroservices.testkit.domain.services.ServiceSpec
import ar.lregnier.akkamicroservices.user.domain.model.User
import ar.lregnier.akkamicroservices.user.domain.services.UserManager.CreateUpdateUserPayload
import ar.lregnier.akkamicroservices.user.domain.services.{UserLookup, UserManager}
import ar.lregnier.akkamicroservices.user.infrastructure.persistence.UserInMemoryRepository
import org.scalatest.OptionValues

// scalastyle:off
class UserManagerItSpec extends ServiceSpec with OptionValues {
  // Fixtures
  val userId = Id.fromUUID(UUID.randomUUID())
  val nonExistentUserId = Id.fromUUID(UUID.randomUUID())
  val firstName = "John"
  val lastName = "Doe"

  trait Scope extends ServiceScope {
    val userRepository = UserInMemoryRepository()
    val userLookup = system.actorOf(UserLookup.props(userRepository))

    val service = system.actorOf(UserManager.props(userRepository))
  }

  "When receiving a CreateEntity msg, it" should {
    "create a new Entity if no errors and return it back" in new Scope {
      // 1- Create Entity
      service ! CreateEntity(CreateUpdateUserPayload(firstName, lastName))

      val createdUser = expectMsgType[User]
      createdUser.firstName shouldEqual firstName
      createdUser.lastName shouldEqual lastName

      // 2- Look up created Entity
      userLookup ! RetrieveEntity(createdUser.id)
      expectMsg(Some(createdUser))
    }
  }

  "When receiving an UpdateEntity msg, it" should {
    "update the requested Entity if no errors and return it back" in new Scope {
      // 1- Create Entity
      service ! CreateEntity(CreateUpdateUserPayload(firstName, lastName))
      val user = expectMsgType[User]

      // 2- Update Entity
      val updatedFirstName = "Richard"
      val updatedLastName = "Roe"

      service ! UpdateEntity(user.id, CreateUpdateUserPayload(updatedFirstName, updatedLastName))

      val updatedEntity = expectMsgType[Option[User]].value
      updatedEntity.id shouldEqual user.id
      updatedEntity.firstName shouldEqual updatedFirstName
      updatedEntity.lastName shouldEqual updatedLastName

      // 3- Look up updated Entity
      userLookup ! RetrieveEntity(updatedEntity.id)
      expectMsg(Some(updatedEntity))

    }
  }

  "When receiving a DeleteEntity msg, it" should {
    "delete the requested Entity if no errors and return it back" in new Scope {
      // 1- Create Entity
      service ! CreateEntity(CreateUpdateUserPayload(firstName, lastName))
      val user = expectMsgType[User]

      // 2- Delete Entity
      service ! DeleteEntity(user.id)
      expectMsg(Some(user))

      // 3- Look up deleted Entity
      userLookup ! RetrieveEntity(user.id)
      expectMsg(None)
    }
  }

}
