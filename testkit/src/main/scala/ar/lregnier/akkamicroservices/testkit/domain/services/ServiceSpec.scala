package ar.lregnier.akkamicroservices.testkit.domain.services

import akka.actor.{ActorRef, ActorSystem}
import akka.testkit._
import org.scalatest.{BeforeAndAfterAll, Matchers, Suite, WordSpecLike}

trait ServiceSpec
    extends ImplicitSystem
    with TestKitBase
    with DefaultTimeout
    with ImplicitSender
    with WordSpecLike
    with Matchers
    with StopSystemAfterAll {

  trait ServiceScope {
    val service: ActorRef

    // CallingThreadDispatcher is used here to make testing execution synchronous
    implicit val ec = system.dispatchers.lookup(CallingThreadDispatcher.Id)
  }

}

trait ImplicitSystem { self: TestKitBase =>
  implicit val system = ActorSystem(systemName)

  def systemName: String = s"${this.getClass().getSimpleName()}-System"
}

trait StopSystemAfterAll extends BeforeAndAfterAll {
  self: TestKitBase with Suite =>

  override def afterAll: Unit =
    TestKit.shutdownActorSystem(system)

}
