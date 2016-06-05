package io.taig.gandalf.ops

import io.taig.gandalf.operator.Mutate
import io.taig.gandalf.syntax.aliases._
import io.taig.gandalf.{ Action, Mutation, Validation }

class mutation[I, O, L <: Mutation.Aux[I, O]]( left: L ) {
    def ~>[P, R <: Action.In[O]]( right: R with Action.Aux[O, P] )(
        implicit
        v: Validation[R]
    ): L <*> R = new Mutate[L, R]( left, right )

    def <*>[P, R <: Action.In[O]]( right: R with Action.Aux[O, P] )(
        implicit
        v: Validation[R]
    ): L <*> R = new Mutate[L, R]( left, right )
}