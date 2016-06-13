package io.taig.gandalf.syntax

import cats.data.ValidatedNel
import io.taig.gandalf.data._

trait aliases {
    class <*>[L <: Mutation, R <: Action.Input[L#Output]] extends Mutate {
        override final type Left = L

        override final type Right = R
    }

    class <~>[L <: Transformation, R <: Action.Input[L#Output]] extends Transform {
        override final type Left = L

        override final type Right = R
    }

    class &[L <: Rule, R <: Rule.Aux[L#Input]] extends EagerAnd {
        override final type Left = L

        override final type Right = R
    }

    class &&[L <: Rule, R <: Rule.Aux[L#Input]] extends LazyAnd {
        override final type Left = L

        override final type Right = R
    }

    class ||[L <: Rule, R <: Rule.Aux[L#Input]] extends Or {
        override final type Left = L

        override final type Right = R
    }

    type Result[+T] = ValidatedNel[String, T]
}

object aliases extends aliases