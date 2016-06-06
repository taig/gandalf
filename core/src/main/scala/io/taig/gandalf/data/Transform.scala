package io.taig.gandalf.data

import io.taig.gandalf.Error.Forward
import io.taig.gandalf._
import io.taig.gandalf.syntax.aliases._
import shapeless.record._

class Transform[L <: Transformation, R <: Action.Input[L#Output]] extends Operation[L, R]

object Transform {
    implicit def validation[O, P, L <: Transformation.Output[O], R <: Action.Aux[O, P]](
        implicit
        l: Validation[O, L],
        r: Validation[P, R],
        e: Error[L <~> R]
    ) = {
        Validation.operation[P, L, R, L <~> R]( l.validate( _ ) andThen r.validate ) {
            Error.forward[L <~> R]( _, _ )
        }
    }

    implicit def error[L <: Transformation, R <: Action.Input[L#Output] with Arguments](
        implicit
        e: Error[R]
    ): Error[L <~> R] = new Error[L <~> R] {
        override def error( arguments: Forward[L <~> R] ) = arguments( "errors" )
    }
}