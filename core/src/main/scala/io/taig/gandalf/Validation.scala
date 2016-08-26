package io.taig.gandalf

trait Validation[V <: Validatable] {
    def validate( input: V#Input ): Result[V#Output]
}

object Validation {
    @inline
    def apply[V <: Validatable]( implicit v: Validation[V] ): Validation[V] = v

    def instance[V <: Validatable](
        f: V#Input ⇒ Result[V#Output]
    ): Validation[V] = new Validation[V] {
        override def validate( input: V#Input ) = f( input )
    }
}