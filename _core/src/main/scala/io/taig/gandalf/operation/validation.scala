package io.taig.gandalf.operation

import io.taig.gandalf._
import io.taig.gandalf.data.Action
import io.taig.gandalf.syntax.aliases._

final class validation[I, O, A <: Validation.Aux[I, O]]( action: A ) {
    def validate( input: I )( implicit v: Validation[O, A] ): Result[O] = v.validate( input )
}