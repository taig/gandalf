package io.taig.gandalf.predef

import io.taig.gandalf._

trait generic {
    final class equal[T <: U: ValueOf, U]
            extends Condition.With[U]( _ == valueOf[T] ) {
        override type Arguments = ( Input, T )

        override def arguments( input: Input ) = ( input, valueOf[T] )
    }

    object equal {
        def apply[T]( value: T ): equal[value.type, T] = {
            new equal[value.type, T]
        }

        implicit def validation[T <: U: ValueOf, U](
            implicit
            e: Error[equal[T, U]]
        ): Validation[equal[T, U]] = {
            Validation.instance[equal[T, U]] {
                new equal[T, U].apply( _ )
            }
        }
    }
}

object generic extends generic