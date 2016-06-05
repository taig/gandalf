package io.taig.gandalf.ops

import io.taig.gandalf.syntax.aliases._
import io.taig.gandalf.{ Action, Validation }

class validation[A <: Action]( action: A ) {
    def validate( input: action.Input )( implicit v: Validation[A] ): Result[A#Output] = {
        v.validate( action )( input )
    }
}