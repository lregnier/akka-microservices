package ar.lregnier.akkamicroservices.testkit.domain.services

import akka.actor.{ActorRef, ActorSystem, Status}
import akka.testkit._
import org.scalamock.scalatest.MockFactory
import org.scalatest.{BeforeAndAfterAll, Matchers, Suite, WordSpecLike}

import scala.reflect.ClassTag

trait ServiceSpec
    extends ImplicitSystem
    with TestKitBase
    with DefaultTimeout
    with ImplicitSender
    with WordSpecLike
    with Matchers
    with MockFactory
    with StopSystemAfterAll {

  trait ServiceScope extends FailureExpectationSupport {
    val service: ActorRef
  }

  trait FailureExpectationSupport { self: ServiceScope =>
    def expectMsgFailure(t: Throwable): Status.Failure =
      expectMsg(Status.Failure(t))

    def expectMsgFailureType[T <: Throwable: ClassTag]: Status.Failure =
      expectMsgPF() {
        case msg @ Status.Failure(_: T) => msg
      }
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
