package io.taig.gandalf.data

import io.taig.gandalf._

class Mutate extends Operator with Mutation {
    override type Left <: Mutation

    override type Right <: Validation.Input[Left#Output]
}

object Mutate {
    type Aux[L <: Mutation, R <: Validation.Input[L#Output]] = Mutate { type Left = L; type Right = R }

    implicit def validation[L, R, M <: Mutate { type Left <: Mutation.Output[L]; type Right <: Validation.Aux[L, R] }](
        implicit
        l: Validation[L, M#Left],
        r: Validation[R, M#Right],
        e: Error[M]
    ) = {
        Validation.operation[R, M]( l.validate( _ ) andThen r.validate ) {
            Error.forward[M]( _, _ )
        }
    }
}