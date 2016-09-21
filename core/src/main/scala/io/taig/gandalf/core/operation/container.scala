package io.taig.gandalf.core.operation

import io.taig.gandalf.core._

final class container[L <: Rule]( left: L ) {
    def validate( input: L#Input )(
        implicit
        v: Validation[L]
    ): Result[L] = v.validate( input )

    def serialize( implicit s: Serialization[L] ): String = s.serialize

    def &&[R <: Rule.Input[L#Output]](
        right: R
    ): L && R = new ( L && R )

    def &[R <: Rule.Aux[L#Input, L#Output]](
        right: R
    ): L & R = new ( L & R )

    def ||[R <: Rule.Aux[L#Input, L#Output]](
        right: R
    ): L || R = new ( L || R )
}