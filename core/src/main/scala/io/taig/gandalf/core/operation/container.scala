package io.taig.gandalf.core.operation

import io.taig.gandalf.core._

final class container[L <: Container]( left: L ) {
    def validate( input: L#Kind#Input )(
        implicit
        v: Validation[L]
    ): Result[L] = v.validate( input )

    def serialize( implicit s: Serialization[L] ): String = s.serialize

    def &&[R <: Container { type Kind <: Rule.Input[L#Kind#Output] }](
        right: R
    ): L && R = new ( L && R )

    def &[R <: Container { type Kind <: Rule.Aux[L#Kind#Input, L#Kind#Output] }](
        right: R
    ): L & R = new ( L & R )

    def ||[R <: Container { type Kind <: Rule.Aux[L#Kind#Input, L#Kind#Output] }](
        right: R
    ): L || R = new ( L || R )
}