package io.taig.gandalf.syntax

import cats.data.ValidatedNel
import io.taig.gandalf.operation._
import io.taig.gandalf._

trait aliases {
    type <*>[L <: Mutation, R <: Action.Input[L#Output]] = Mutate[L, R]

    type <~>[L <: Transformation, R <: Action.Input[L#Output]] = Transform[L, R]

    type &[L <: Rule, R <: Rule.Aux[L#Input]] = EagerAnd[L, R]

    type &&[L <: Rule, R <: Rule.Aux[L#Input]] = LazyAnd[L, R]

    type ||[L <: Rule, R <: Rule.Aux[L#Input]] = Or[L, R]

    type Result[+T] = ValidatedNel[String, T]
}

object aliases extends aliases