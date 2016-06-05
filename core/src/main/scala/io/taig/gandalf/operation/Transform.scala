package io.taig.gandalf.operation

import io.taig.gandalf._
import io.taig.gandalf.syntax.aliases._

class Transform[L <: Transformation, R <: Action.Input[L#Output]] extends Operation[L, R]

object Transform {
    implicit def validation[O, P, L <: Transformation.Output[O], R <: Action.Aux[O, P]](
        implicit
        l: Validation[O, L],
        r: Validation[P, R]
    ): Validation[P, L <~> R] = {
        Validation.instance { input ⇒
            l.validate( input ) andThen r.validate
        }
    }
}