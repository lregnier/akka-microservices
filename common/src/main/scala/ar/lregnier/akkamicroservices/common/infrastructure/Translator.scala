package ar.lregnier.akkamicroservices.common.infrastructure

trait Translator[-From, +To] {
  def translate(from: From): To
}
