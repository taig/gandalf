package io.taig.gandalf.ops

import io.taig.gandalf._
import io.taig.gandalf.syntax.aliases._

class validation[V <: Validation]( validation: V )(
        implicit
        ev: Evaluation[V]
) {
    def validate( value: V#Input ): Result[V#Output] = ev.validate( value )
}