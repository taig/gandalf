package io.taig.gandalf.data

import io.taig.gandalf._
import io.taig.gandalf.syntax.aliases._

class Mutate[L <: Mutation, R <: Action.Input[L#Output]] extends Operation[L, R]

object Mutate {
    implicit def validation[O, P, L <: Mutation.Output[O], R <: Action.Aux[O, P]](
        implicit
        l: Validation[O, L],
        r: Validation[P, R],
        e: Error[L <*> R]
    ) = {
        Validation.operation[P, L, R, L <*> R]( l.validate( _ ) andThen r.validate ) {
            Error.forward[L <*> R]( _, _ )
        }
    }
}