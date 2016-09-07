package io.taig.gandalf.predef

import io.taig.gandalf.Rule.Applyable
import io.taig.gandalf._

trait string {
    object ltrim extends Transformation.With[String, String](
        _.replaceFirst( "^\\s*", "" )
    )

    object nonEmpty
        extends Condition.With[String]( _.nonEmpty )
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

    object required extends ( trim.type ~> nonEmpty.type )

    object rtrim extends Transformation.With[String, String](
        _.replaceFirst( "\\s*$", "" )
    )

    object toLower extends Transformation.With[String, String]( _.toLowerCase )

    object toUpper extends Transformation.With[String, String]( _.toUpperCase )
}

object string extends string