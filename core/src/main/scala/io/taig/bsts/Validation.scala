package io.taig.bsts

trait Validation[I, O] {
    type F

    def validate( input: I ): Result[F, O]
}

object Validation {
    type Aux[I, O, Out0] = Validation[I, O] { type Out = Out0 }
}