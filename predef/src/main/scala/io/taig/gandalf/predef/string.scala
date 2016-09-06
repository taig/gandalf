package io.taig.gandalf.predef

import io.taig.gandalf.Rule.Arguments
import io.taig.gandalf._

trait string {
    object nonEmpty
        extends Condition.With[String]( _.nonEmpty )
        with Arguments.Input

    object trim extends Transformation.With[String, String]( _.trim )

    final class regex[T <: String: ValueOf]
            extends Condition.With[String]( _.matches( valueOf[T] ) )
            with Arguments.With[String] {
        override def arguments( input: String ) = ( input, valueOf[T] )
    }

    object regex {
        def apply( value: String ): regex[value.type] = new regex[value.type]

        implicit def validation[T <: String: ValueOf](
            implicit
            e: Error[regex[T]]
        ): Validation[regex[T]] = {
            Validation.instance[regex[T]] {
                new regex[T].apply( _ )
            }
        }
    }

    object required extends ( trim.type ~> nonEmpty.type )

    object toLower extends Transformation.With[String, String]( _.toLowerCase )

    object toUpper extends Transformation.With[String, String]( _.toUpperCase )
}

object string extends string