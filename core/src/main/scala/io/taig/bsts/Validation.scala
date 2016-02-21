package io.taig.bsts

import cats.data.Validated
import shapeless.HList

trait Validation[I, O] {
    type V <: HList

    type E

    /**
     * Contained (nested) Validations, may only include itself (see Term)
     */
    def validations: V

    def validate( input: I ): Validated[E, O]
}

object Validation {
    type Aux0[I, O, V0 <: HList] = Validation[I, O] { type V = V0 }

    type Aux[I, O, V0 <: HList, E0] = Validation.Aux0[I, O, V0] { type E = E0 }
}