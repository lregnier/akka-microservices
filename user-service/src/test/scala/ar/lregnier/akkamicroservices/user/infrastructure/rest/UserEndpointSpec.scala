package ar.lregnier.akkamicroservices.user.infrastructure.rest

import java.util.UUID

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.model.headers.Location
import akka.testkit.TestProbe
import ar.lregnier.akkamicroservices.common.domain.model.Id
import ar.lregnier.akkamicroservices.common.domain.services._
import ar.lregnier.akkamicroservices.testkit.infrastructure.rest.RestEndpointSpec
import ar.lregnier.akkamicroservices.user.domain.model.User
import ar.lregnier.akkamicroservices.user.domain.services.UserManager.CreateUpdateUserPayload
import ar.lregnier.user.infrastructure.rest.UserEndpoint
import ar.lregnier.user.infrastructure.rest.UserEndpoint.{CreateUpdateUserRepresentation, UserRepresentation}

// scalastyle:off
class UserEndpointSpec extends RestEndpointSpec {

  // Fixtures
  val userId = Id(UUID.randomUUID().toString)
  val nonExistentUserId = Id(UUID.randomUUID().toString)
  val firstName = "First name"
  val lastName = "Last name"
  val user = User(userId, firstName, lastName)
  val userRepresentation = UserRepresentation(userId.value, firstName, lastName)

  trait Scope extends RestEndpointScope {
    val userManager = TestProbe()
    val userLookup = TestProbe()

    val routes = UserEndpoint(userManager.ref, userLookup.ref).routes
  }

  "When sending a POST request, the User API" should {
    "create a new User and return a 201 Response if the request is valid" in new Scope {
      val result = Post("/users", CreateUpdateUserRepresentation(firstName, lastName)).run

      userManager.expectMsg(CreateEntity(CreateUpdateUserPayload(firstName, lastName)))
      userManager.reply(user)

      result.check {
        response.status shouldEqual StatusCodes.Created
        header[Location] shouldBe defined
      }
    }
    "not create a User and return a 400 Response if the request is not valid" in new Scope {
      val emptyFirstName = "" // First name must not be empty
      val result = Post("/users", Map("firstName" -> emptyFirstName, "lastName" -> lastName)).runSeal

      userManager.expectNoMsg()

      result.check {
        response.status shouldEqual StatusCodes.BadRequest
      }
    }
  }

  "When sending a GET request, the User API" should {
    "return a 200 Response with the requested User if it exists" in new Scope {
      val result = Get(s"/users/$userId").run

      userLookup.expectMsg(RetrieveEntity(userId))
      userLookup.reply(Some(user))

      result.check {
        response.status shouldEqual StatusCodes.OK
        responseAs[UserRepresentation] shouldEqual userRepresentation
      }
    }
    "return a 404 Response if the requested User does not exist" in new Scope {
      val result = Get(s"/users/$nonExistentUserId").runSeal

      userLookup.expectMsg(RetrieveEntity(nonExistentUserId))
      userLookup.reply(None)

      result.check {
        response.status shouldEqual StatusCodes.NotFound
      }
    }
  }

  "When sending a PUT request, the User API" should {
    "update the User with the given data and return it back in a 200 Response" in new Scope {
      val updatedId = user.id
      val updatedFirstName = "Updated first name"
      val updatedLastName = "Updated last name"

      val result = Put(s"/users/$updatedId", CreateUpdateUserRepresentation(updatedFirstName, updatedLastName)).run
      userManager.expectMsg(UpdateEntity(updatedId, CreateUpdateUserPayload(updatedFirstName, updatedLastName)))
      userManager.reply(Some(User(updatedId, updatedFirstName, updatedLastName)))

      result.check {
        response.status shouldEqual StatusCodes.OK
        responseAs[UserRepresentation] shouldEqual UserRepresentation(
          updatedId.value,
          updatedFirstName,
          updatedLastName
        )
      }
    }
    "not update the User and return a 400 Response if the request is not valid" in new Scope {
      val updatedId = user.id
      val firstName = "" // Name must not be empty
      val lastName = "Updated last name"

      val result = Put(s"/users/$updatedId", Map("firstName" -> firstName, "lastName" -> lastName)).runSeal
      userManager.expectNoMsg()

      result.check {
        response.status shouldEqual StatusCodes.BadRequest
      }
    }
    "return a 404 Response if the requested User does not exist" in new Scope {
      val updatedId = nonExistentUserId
      val updatedFirstName = "Updated first name"
      val updatedLastName = "Updated last name"

      val result = Put(s"/users/$updatedId", CreateUpdateUserRepresentation(updatedFirstName, updatedLastName)).run

      userManager.expectMsg(UpdateEntity(updatedId, CreateUpdateUserPayload(updatedFirstName, updatedLastName)))
      userManager.reply(None)

      result.check {
        response.status shouldEqual StatusCodes.NotFound
      }
    }
  }

  "When sending a DELETE request, the User API" should {
    "delete the requested User and return a 204 Response if the User exists" in new Scope {
      val result = Delete(s"/users/$userId").run

      userManager.expectMsg(DeleteEntity(userId))
      userManager.reply(Some(user))

      result.check {
        response.status shouldEqual StatusCodes.NoContent
      }
    }
    "return a 404 Response if the requested User does not exist" in new Scope {
      val result = Delete(s"/users/$nonExistentUserId").runSeal

      userManager.expectMsg(DeleteEntity(nonExistentUserId))
      userManager.reply(None)

      result.check {
        response.status shouldEqual StatusCodes.NotFound
      }
    }
  }

  "When sending a GET request, the User API" should {
    "return a list of all Users" in new Scope {
      val users = Seq(user)
      val userRepresentations = Seq(userRepresentation)

      val result = Get("/users").run

      userLookup.expectMsg(ListEntities)
      userLookup.reply(users)

      result.check {
        response.status shouldEqual StatusCodes.OK
        responseAs[Seq[UserRepresentation]] should contain theSameElementsAs (userRepresentations)
      }
    }
  }
}
