package io.taig.gandalf.data

import io.taig.gandalf._

class Transform extends Operation with Transformation {
    override type Left <: Transformation
}

object Transform {
    type Aux[L <: Transformation, R <: Action.Input[L#Output]] = Transform { type Left = L; type Right = R }

    implicit def validation[L, R, T <: Transform { type Left <: Transformation.Output[L]; type Right <: Action.Aux[L, R] }](
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