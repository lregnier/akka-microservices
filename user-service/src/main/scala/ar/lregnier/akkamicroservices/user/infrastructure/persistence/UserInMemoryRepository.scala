package ar.lregnier.akkamicroservices.user.infrastructure.persistence

import ar.lregnier.akkamicroservices.common.infrastructure.persistence.InMemoryRepository
import ar.lregnier.akkamicroservices.user.domain.model.User
import ar.lregnier.akkamicroservices.user.domain.persistence.UserRepository

class UserInMemoryRepository extends UserRepository with InMemoryRepository[User]

object UserInMemoryRepository {
  def apply(): UserInMemoryRepository = new UserInMemoryRepository()
}
