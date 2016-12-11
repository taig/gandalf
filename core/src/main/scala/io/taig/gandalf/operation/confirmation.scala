package io.taig.gandalf.operation

import io.taig.gandalf._

final class confirmation[I]( val input: I ) extends AnyVal {
    def confirm[R <: Rule, O]( rule: R )(
        implicit
        v: Validation[R, I, O]
    ): Option[O] = v.confirm( input )
}