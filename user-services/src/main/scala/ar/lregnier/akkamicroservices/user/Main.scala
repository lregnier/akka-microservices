import akka.routing.FromConfig
import ar.lregnier.akkamicroservices.common.infrastructure.cluster.AkkaServer
import ar.lregnier.akkamicroservices.common.infrastructure.rest.RestServer
import ar.lregnier.akkamicroservices.user.domain.services.{UserLookup, UserManager}
import ar.lregnier.user.infrastructure.rest.UserEndpoint

trait AkkaModule {
  implicit val actorSystem = AkkaServer.start()
}

trait DomainModule { self: AkkaModule =>
  lazy val userManager = actorSystem.actorOf(FromConfig.props(UserManager.props()), UserManager.Name)
  lazy val userLookup = actorSystem.actorOf(FromConfig.props(UserManager.props()), UserLookup.Name)
}

trait RestModule { self: AkkaModule with DomainModule =>
  lazy val endpoints =
    Seq(
      UserEndpoint(userManager, userLookup)
    )

  RestServer.start(endpoints)
}

object Main extends App with AkkaModule with DomainModule with RestModule
