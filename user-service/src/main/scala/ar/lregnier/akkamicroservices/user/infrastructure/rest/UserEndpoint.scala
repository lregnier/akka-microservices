package ar.lregnier.user.infrastructure.rest

import akka.actor.ActorRef
import akka.http.scaladsl.server.Route
import ar.lregnier.akkamicroservices.common.infrastructure.Translator
import ar.lregnier.akkamicroservices.common.infrastructure.rest.{CRUDRoutes, Representation, RestEndpoint}
import ar.lregnier.akkamicroservices.user.domain.model.User
import ar.lregnier.akkamicroservices.user.domain.services.UserManager.CreateUpdateUserPayload

object UserEndpoint {
  def apply(userManager: ActorRef, userLookup: ActorRef): UserEndpoint =
    new UserEndpoint(userManager, userLookup)

  // Representations
  case class CreateUpdateUserRepresentation(firstName: String, lastName: String) extends Representation {
    require(firstName.nonEmpty)
    require(lastName.nonEmpty)
  }

  case class UserRepresentation(id: String, firstName: String, lastName: String) extends Representation

  // Translations
  implicit val toCreateUpdateUserPayload = new Translator[CreateUpdateUserRepresentation, CreateUpdateUserPayload] {
    override def translate(from: CreateUpdateUserRepresentation): CreateUpdateUserPayload =
      CreateUpdateUserPayload(firstName = from.firstName, lastName = from.lastName)
  }

  implicit val toUserRepresentation = new Translator[User, UserRepresentation] {
    override def translate(from: User): UserRepresentation =
      UserRepresentation(id = from.id.value, firstName = from.firstName, lastName = from.lastName)
  }
}

class UserEndpoint(userManager: ActorRef, userLookup: ActorRef) extends RestEndpoint with CRUDRoutes {
  import UserEndpoint._

  override def routes: Route =
    pathPrefix("users") {
      crudRoutes(userManager, userLookup)
    }
}
