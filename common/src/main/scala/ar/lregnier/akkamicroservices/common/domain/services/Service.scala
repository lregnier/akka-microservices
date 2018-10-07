package ar.lregnier.akkamicroservices.common.domain.services

import akka.actor.{Actor, ActorLogging}
import akka.pattern.PipeToSupport
import ar.lregnier.akkamicroservices.common.utils.DefaultAskSupport

trait Service extends Actor with ActorLogging with DefaultAskSupport with PipeToSupport {
  implicit val ec = context.dispatcher
}
