package io.taig.gandalf.typelevel

/**
 * Operations allow to combine Rules
 */
trait Operation[L <: Rule, R <: Rule.Aux[L#Output]] extends Rule {
    override final type Input = L#Input
}

object Operation {
    implicit val error = Error.none[_ Operation _]
}