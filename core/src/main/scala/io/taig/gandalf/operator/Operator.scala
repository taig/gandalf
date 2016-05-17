package io.taig.gandalf.operator

import io.taig.gandalf.{ Error, Rule }

trait Operator[L <: Rule, R <: Rule.Aux[L#Output]] extends Rule {
    override final type Input = L#Input
}

object Operator {
    implicit val error = Error.none[_ Operator _]
}