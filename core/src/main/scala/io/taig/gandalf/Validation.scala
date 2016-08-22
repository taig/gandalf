package io.taig.gandalf

trait Validation[I, O, -V <: Validatable.Aux[I, O]] {
    def validate( input: I ): Result[O]
}

object Validation {
    @inline
    def apply[V <: Validatable]( implicit v: Validation[V] ): Validation[V] = v
}