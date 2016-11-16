package io.taig.gandalf

import cats.data._

package object core {
    type Result[C <: Container] = ValidatedNel[String, C#Kind#Output]

    def tryLift[C <: Container]( container: C )( input: C#Kind#Input )(
        implicit
        v: Validation[C]
    ): ValidatedNel[String, C#Kind#Input Obeys C] = {
        v.validate( input ).map( Obeys.applyUnsafe )
    }
}