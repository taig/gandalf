package io.taig.gandalf.predef.string

import io.taig.gandalf.core.{ Condition, Reportable }
import io.taig.gandalf.core.Rule.Applyable

final class regex[T <: String: ValueOf]
        extends Condition.With[String]( _.matches( valueOf[T] ) )
        with Reportable.With[String] {
    override def arguments( input: String ) = ( input, valueOf[T] )
}

object regex {
    def apply( value: String ): regex[value.type] = new regex[value.type]

    implicit def implicits[T <: String: ValueOf] = {
        Applyable.implicits[regex[T]]( new regex[T] )
    }
}