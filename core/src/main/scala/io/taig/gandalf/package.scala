package io.taig

import cats.data._

package object gandalf {
    type Result[V <: Validatable] = ValidatedNel[String, V#Output]

    class &&[L <: Rule, R <: Rule.Aux[L#Output]] extends LazyAnd {
        override final type Left = L

        override final type Right = R
    }

    class &[L <: Rule, R <: Rule.Aux[L#Output]] extends EagerAnd {
        override final type Left = L

        override final type Right = R
    }

    class ||[L <: Rule, R <: Rule.Aux[L#Output]] extends Or {
        override final type Left = L

        override final type Right = R
    }

    class ~>[L <: Mutation, R <: Validatable.Input[L#Output]] extends Mutate {
        override final type Left = L

        override final type Right = R
    }
}