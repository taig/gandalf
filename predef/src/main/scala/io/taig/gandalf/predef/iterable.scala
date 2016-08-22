package io.taig.gandalf.predef

import io.taig.gandalf._

trait iterable {
    sealed class nonEmpty[T] extends Rule with Input[Iterable[T]] with Arguments.Input {
        override def check( input: Iterable[T] ) = input.nonEmpty

        override def arguments( input: Iterable[T] ) = input
    }

    object nonEmpty {
        @inline
        def apply[T]: nonEmpty[T] = new nonEmpty[T]

        implicit def validation[T]: Validation[nonEmpty[T]] = {
            new Validation[nonEmpty[T]] {
                override def validate( input: nonEmpty[T]#Input ) = {
                    nonEmpty[T].apply( input )
                }
            }
        }
    }
}

object iterable extends iterable