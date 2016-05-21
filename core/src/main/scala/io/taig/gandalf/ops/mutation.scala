package io.taig.gandalf.ops

import io.taig.gandalf.operator.Apply
import io.taig.gandalf.{ Mutation, Validation }

class mutation[I, O, L <: Mutation.Aux[I, O]]( left: L ) {
    def ~>[P, R <: Validation.In[O]]( right: R with Validation.Aux[O, P] ): L Apply R = new Apply[L, R]
}