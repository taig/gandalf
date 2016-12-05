package io.taig.gandalf.core.operation

import io.taig.gandalf.core.{ Rule, Validation }

final class validation[I]( val input: I ) extends AnyVal {
    def confirm[R <: Rule, O]( right: R )(
        implicit
        v: Validation[R, I, O]
    ): Option[O] = v.confirm( input )
}