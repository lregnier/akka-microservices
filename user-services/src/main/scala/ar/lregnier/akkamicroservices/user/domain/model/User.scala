package ar.lregnier.akkamicroservices.user.domain.model

import ar.lregnier.akkamicroservices.common.domain.model.{Entity, Id}

case class User(id: Id, firstName: String, lastName: String) extends Entity
