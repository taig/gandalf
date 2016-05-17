package io.taig.gandalf.ops

import io.taig.gandalf.{ Apply, Evaluation, Mutation, Validation }

class mutation[L <: Mutation]( left: Evaluation[L] ) {
    def ~>[R <: Validation.In[L#Output]]( right: Evaluation[R] ): L Apply R = Apply( left, right )
}