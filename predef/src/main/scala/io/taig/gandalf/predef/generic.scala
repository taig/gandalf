package io.taig.gandalf.predef

import io.taig.gandalf.{ Definition, Input, Rule }

import scala.language.higherKinds

trait generic {
    @Definition
    sealed class eq[T, U <: T: ValueOf] extends Rule with Input[T] {
        override type Arguments = ( T, U )

        override def check( input: T ) = input == valueOf[U]

        override def arguments( input: T ) = ( input, valueOf[U] )
    }
}

object generic extends generic