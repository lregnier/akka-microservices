package ar.lregnier.akkamicroservices.common.infrastructure.rest

import akka.actor.ActorRef
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.model.headers.Location
import akka.http.scaladsl.server.{Directives, Route}
import ar.lregnier.akkamicroservices.common.domain.model.{DomainObject, Entity, Id}
import ar.lregnier.akkamicroservices.common.domain.services._
import ar.lregnier.akkamicroservices.common.infrastructure.Translator
import ar.lregnier.akkamicroservices.common.utils.DefaultAskSupport

import scala.reflect.ClassTag

// scalastyle:off
trait CRUDRoutes {
  self: Directives with Json4sJacksonSupport with DefaultAskSupport with ResponseHandling =>

  def create[In <: Representation, D <: CreateEntityPayload, E <: Entity: ClassTag, Out <: Representation](
      managerActor: ActorRef
  )(implicit requestTranslator: Translator[In, D], responseTranslator: Translator[E, Out], m: Manifest[In]): Route =
    (pathEnd & post & entity(as[In])) { representation =>
      extractUri { uri =>
        val payload = requestTranslator.translate(representation)
        val msg = CreateEntity(payload)
        onSuccess((managerActor ? msg).mapTo[E]) { entity =>
          respondWithHeader(Location(s"$uri/${entity.id}")) {
            respond(StatusCodes.Created, entity)
          }
        }
      }
    }

  def retrieve[E <: DomainObject: ClassTag, Out <: Representation](
      lookupActor: ActorRef
  )(implicit responseTranslator: Translator[E, Out]): Route =
    (path(JavaUUID) & get) { id =>
      onSuccess((lookupActor ? RetrieveEntity(Id.fromUUID(id))).mapTo[Option[E]]) {
        case Some(entity) => respond(entity)
        case None         => respond(StatusCodes.NotFound)
      }
    }

  def update[In <: Representation, Data <: UpdateEntityPayload, E <: Entity: ClassTag, Out <: Representation](
      managerActor: ActorRef
  )(implicit requestTranslator: Translator[In, Data], responseTranslator: Translator[E, Out], m: Manifest[In]): Route =
    (path(JavaUUID) & put & entity(as[In])) { (id, representation) =>
      val payload = requestTranslator.translate(representation)
      val msg = UpdateEntity(Id.fromUUID(id), payload)
      onSuccess((managerActor ? msg).mapTo[Option[E]]) {
        case Some(entity) => respond(entity)
        case None         => respond(StatusCodes.NotFound)
      }
    }

  def remove[E <: Entity: ClassTag](managerActor: ActorRef): Route =
    (path(JavaUUID) & delete) { id =>
      onSuccess((managerActor ? DeleteEntity(Id.fromUUID(id))).mapTo[Option[E]]) {
        case Some(_) => respond(StatusCodes.NoContent)
        case None    => respond(StatusCodes.NotFound)
      }
    }

  def list[E <: Entity: ClassTag, Out <: Representation](
      lookupActor: ActorRef
  )(implicit responseTranslator: Translator[E, Out]): Route =
    (pathEnd & get) {
      onSuccess((lookupActor ? ListEntities).mapTo[Seq[E]]) { entities =>
        respond(entities)
      }
    }

  def crudRoutes[
      CreateRep <: Representation,
      CreatePay <: CreateEntityPayload,
      UpdateRep <: Representation,
      UpdateEnt <: UpdateEntityPayload,
      E <: Entity: ClassTag,
      Out <: Representation
  ](managerActor: ActorRef, lookupActor: ActorRef)(
      implicit createRequestTranslator: Translator[CreateRep, CreatePay],
      updateRequestTranslator: Translator[UpdateRep, UpdateEnt],
      responseTranslator: Translator[E, Out],
      mCreate: Manifest[CreateRep],
      mUpdate: Manifest[UpdateRep]
  ): Route =
    create[CreateRep, CreatePay, E, Out](managerActor) ~
      retrieve[E, Out](lookupActor) ~
      update[UpdateRep, UpdateEnt, E, Out](managerActor) ~
      remove[E](managerActor) ~
      list(lookupActor)

}
