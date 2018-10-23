package ar.lregnier.akkamicroservices.user.domain.persistence

import ar.lregnier.akkamicroservices.common.domain.persistence.Repository
import ar.lregnier.akkamicroservices.user.domain.model.User

trait UserRepository extends Repository[User]
