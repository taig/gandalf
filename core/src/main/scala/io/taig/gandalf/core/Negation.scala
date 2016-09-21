package io.taig.gandalf.core

trait Negation[R <: Rule] {
    def negate( input: R#Input ): Result[R]
}

object Negation {
    @inline
    def apply[R <: Rule]( implicit n: Negation[R] ): Negation[R] = n

    def instance[R <: Rule](
        f: R#Input ⇒ Result[R]
    ): Negation[R] = new Negation[R] {
        override def negate( input: R#Input ) = f( input )
    }

    /**
     * not( not( condition ) ) -> condition
     */
    implicit def validation[R <: Rule](
        implicit
        v: Validation[R]
    ): Negation[not[R]] = Negation.instance[not[R]]( v.validate )
}