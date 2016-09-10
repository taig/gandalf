package io.taig.gandalf.predef

import io.taig.gandalf.core.Rule.Applyable
import io.taig.gandalf.core._

trait string {
    object ltrim extends Transformation.With[String, String](
        _.replaceFirst( "^\\s*", "" )
    )

    object isEmpty
        extends Condition.With[String]( _.isEmpty )
        with Reportable.Input

    object trim extends Transformation.With[String, String]( _.trim )

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

    object required extends ( trim.type ~> not[isEmpty.type] )

    object rtrim extends Transformation.With[String, String](
        _.replaceFirst( "\\s*$", "" )
    )

    object toLower extends Transformation.With[String, String]( _.toLowerCase )

    object toUpper extends Transformation.With[String, String]( _.toUpperCase )
}

object string extends string