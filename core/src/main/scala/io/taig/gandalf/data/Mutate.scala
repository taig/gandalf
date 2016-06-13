package io.taig.gandalf.data

import io.taig.gandalf._

import scala.language.reflectiveCalls

class Mutate extends Operation {
    override type Left <: Mutation

    override type Right <: Action.Input[Left#Output]
}

object Mutate {
    type Aux[L <: Mutation, R <: Action.Input[L#Output]] = Mutate { type Left = L; type Right = R }

    implicit def validation[L, R, M <: Mutate { type Left <: Mutation.Output[L]; type Right <: Action.Aux[L, R] }](
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