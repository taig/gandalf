package io.taig.gandalf.syntax

import cats.data.ValidatedNel
import io.taig.gandalf.operator._
import io.taig.gandalf.{ Mutation, Rule, Validation }

trait aliases {
    type ~>[L <: Mutation, R <: Validation.In[L#Output]] = Apply[L, R]

    type &[L <: Rule, R <: Rule.Aux[L#Input]] = EagerAnd[L, R]

    type &&[L <: Rule, R <: Rule.Aux[L#Input]] = LazyAnd[L, R]

    type ||[L <: Rule, R <: Rule.Aux[L#Input]] = Or[L, R]

    type Result[+T] = ValidatedNel[String, T]
}

object aliases extends aliases