package io.taig.gandalf.operation

import io.taig.gandalf._
import io.taig.gandalf.syntax.aliases._

class Mutate[L <: Mutation, R <: Action.Input[L#Output]] extends Operation[L, R]

object Mutate {
    implicit def validation[O, P, L <: Mutation.Output[O], R <: Action.Aux[O, P]](
        implicit
        l: Validation[O, L],
        r: Validation[P, R]
    ): Validation[P, L <*> R] = {
        Validation.instance { input ⇒
            l.validate( input ) andThen r.validate
        }
    }
}