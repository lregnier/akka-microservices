package ar.lregnier.akkamicroservices.user.domain.services

import java.util.UUID

import akka.testkit.TestActorRef
import ar.lregnier.akkamicroservices.common.domain.model.Id
import ar.lregnier.akkamicroservices.common.domain.services.{ListEntities, RetrieveEntity}
import ar.lregnier.akkamicroservices.testkit.domain.services.ServiceSpec
import ar.lregnier.akkamicroservices.user.domain.model.User

class UserLookupUnitSpec extends ServiceSpec {

  // Fixtures
  val userId = Id(UUID.randomUUID().toString)

  trait Scope extends ServiceScope {
    val service = TestActorRef(new UserLookup())
  }

  "When sending a RetrieveEntity msg, the service" should {
    "respond with the requested Entity if it exists" in new Scope {
      val id = userId
      service ! RetrieveEntity(id)
      expectMsgPF() {
        case Some(User(requestedId, _, _)) =>
          requestedId shouldEqual id
      }
    }
  }

  "When sending a ListEntities msg, the service" should {
    "respond with all the known Users" in new Scope {
      service ! ListEntities
      expectMsgPF() {
        case _: Seq[User] => succeed
      }
    }
  }

}
