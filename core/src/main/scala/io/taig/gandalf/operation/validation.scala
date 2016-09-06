package io.taig.gandalf.operation

import io.taig.gandalf._

final class validation[R <: Rule]( rule: R ) {
    def validate( input: R#Input )(
        implicit
        v: Validation[R]
    ): Result[R] = v.validate( input )
}