package io.taig.gandalf.operation

import io.taig.gandalf._

final case class mutation[L <: Mutation]( left: L ) {
    def ~>[R <: Validatable.Input[L#Output]]( right: R ) = {
        new Mutate {
            override type Left = L

            override type Right = R
        }
    }

    def |>[R <: Rule.Aux[L#Output]]( right: R ) = {
        new Asdf {
            override type Left = L

            override type Right = R
        }
    }
}