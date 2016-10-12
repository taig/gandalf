package io.taig.gandalf.core

trait Validation[C <: Container] {
    def validate( input: C#Kind#Input ): Result[C]
}

object Validation {
    @inline
    def apply[C <: Container]( implicit v: Validation[C] ): Validation[C] = v

    def instance[C <: Container](
        f: C#Kind#Input ⇒ Result[C]
    ): Validation[C] = new Validation[C] {
        override def validate( input: C#Kind#Input ) = f( input )
    }
}