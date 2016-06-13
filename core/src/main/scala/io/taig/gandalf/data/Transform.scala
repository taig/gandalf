package io.taig.gandalf.data

import io.taig.gandalf.Error.Forward
import io.taig.gandalf._
import io.taig.gandalf.syntax.aliases._
import shapeless.record._

class Transform extends Operation {
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

    implicit def error[L <: Transformation, R <: Action.Input[L#Output] with Arguments](
        implicit
        e: Error[R]
    ): Error[L <~> R] = new Error[L <~> R] {
        override def error( arguments: Forward[L <~> R] ) = arguments( "errors" )
    }
}