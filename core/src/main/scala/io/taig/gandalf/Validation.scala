package io.taig.gandalf

trait Validation[R <: Rule] {
    def validate( input: R#Input ): Result[R]
}

object Validation {
    @inline
    def apply[R <: Rule]( implicit v: Validation[R] ): Validation[R] = v

    def instance[R <: Rule](
        f: R#Input ⇒ Result[R]
    ): Validation[R] = new Validation[R] {
        override def validate( input: R#Input ) = f( input )
    }
}