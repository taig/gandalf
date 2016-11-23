package io.taig.gandalf.core.operation

import io.taig.gandalf.core.{ Rule, Validation }

final class generic[I]( val input: I ) extends AnyVal {
    def confirm[R <: Rule, O]( rule: R )(
        implicit
        v: Validation[R, I, O]
    ): Option[O] = v( input )
}