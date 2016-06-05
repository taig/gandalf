package io.taig.gandalf.operation

import io.taig.gandalf._
import io.taig.gandalf.data.Action
import io.taig.gandalf.syntax.aliases._

final class validation[O, A <: Action.Output[O]]( action: A ) {
    def validate( input: A#Input )( implicit v: Validation[O, A] ): Result[O] = v.validate( input )
}