package io.taig.gandalf.operation

import io.taig.gandalf.data.{ Action, Mutate, Mutation }
import io.taig.gandalf.syntax.aliases._

final class mutation[I, O, L <: Mutation.Aux[I, O]]( left: L ) {
    def ~>[P, R <: Action.Input[O]]( right: R with Action.Aux[O, P] ): L <*> R = new Mutate[L, R]

    def <*>[P, R <: Action.Input[O]]( right: R with Action.Aux[O, P] ): L <*> R = new Mutate[L, R]
}