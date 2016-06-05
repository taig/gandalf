package io.taig.gandalf.playground

import io.taig.gandalf.{ Action, Rule }

import scala.language.existentials

trait And[L <: Rule, R <: Rule.Aux[L#Output]] extends Action {
    override final type Input = L#Input

    override final type Output = R#Output
}