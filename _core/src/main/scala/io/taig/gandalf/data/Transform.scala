package io.taig.gandalf.data

import io.taig.gandalf._

class Transform extends Operator with Mutation {
    override type Left <: Mutation
}

object Transform {
    type Aux[L <: Mutation, R <: Validation.Input[L#Output]] = Transform { type Left = L; type Right = R }

    implicit def validation[L, R, T <: Transform { type Left <: Mutation.Output[L]; type Right <: Validation.Aux[L, R] }](
        implicit
        l: Validation[L, T#Left],
        r: Validation[R, T#Right],
        e: Error[T]
    ) = {
        Validation.operation[R, T]( l.validate( _ ) andThen r.validate ) {
            Error.forward[T]( _, _ )
        }
    }
}