package io.taig.gandalf.operation

import io.taig.gandalf.data.{ Action, Mutate, Mutation }
import io.taig.gandalf.syntax.aliases._

final class mutation[I, O, L <: Mutation.Aux[I, O]]( left: L ) {
    def ~>[P, R <: Action.Input[O]]( right: R with Action.Aux[O, P] ): Mutate.Aux[L, R] = new <*>[L, R]

    def <*>[P, R <: Action.Input[O]]( right: R with Action.Aux[O, P] ): Mutate.Aux[L, R] = ~>( right )
}