import akka.routing.FromConfig
import ar.lregnier.akkamicroservices.common.infrastructure.cluster.AkkaServer
import ar.lregnier.akkamicroservices.common.infrastructure.rest.RestServer
import ar.lregnier.akkamicroservices.user.domain.services.{UserLookup, UserManager}
import ar.lregnier.akkamicroservices.user.infrastructure.persistence.UserInMemoryRepository
import ar.lregnier.user.infrastructure.rest.UserEndpoint

trait AkkaModule {
  implicit val actorSystem = AkkaServer.start()
}

trait PersistenceModule {
  lazy val userRepository = UserInMemoryRepository()
}

trait DomainModule { self: AkkaModule with PersistenceModule =>
  lazy val userManager = actorSystem.actorOf(FromConfig.props(UserManager.props(userRepository)), UserManager.Name)
  lazy val userLookup = actorSystem.actorOf(FromConfig.props(UserLookup.props(userRepository)), UserLookup.Name)
}

trait RestModule { self: AkkaModule with DomainModule =>
  lazy val endpoints =
    Seq(
      UserEndpoint(userManager, userLookup)
    )

  RestServer.start(endpoints)
}

object Main extends App with AkkaModule with PersistenceModule with DomainModule with RestModule
