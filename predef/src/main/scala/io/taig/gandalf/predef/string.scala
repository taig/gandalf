package io.taig.gandalf.predef

import io.taig.gandalf.core.Rule.Applyable
import io.taig.gandalf.core._

trait string {
    class contains[T <: String: ValueOf]
        extends Condition.With[String]( _.contains( valueOf[T] ) )
        with Arguments.With[T] {
        override val argument = valueOf[T]
    }

    object contains {
        def apply( value: String ): contains[value.type] = {
            new contains[value.type]
        }

        implicit def implicits[T <: String: ValueOf] = {
            Applyable.implicits[contains[T]]( new contains[T] )
        }
    }

    object email extends matches["^[^@]+@[^@]+\\.[^@]+$"]

    object empty
        extends Condition.With[String]( _.isEmpty )
        with Arguments.Input

    object ltrim extends Transformation.With[String, String](
        _.replaceFirst( "^\\s*", "" )
    )

    class matches[T <: String: ValueOf]
            extends Condition.With[String]( _.matches( valueOf[T] ) )
            with Arguments.With[T] {
        override val argument = valueOf[T]
    }

    object matches {
        def apply( value: String ): matches[value.type] = {
            new matches[value.type]
        }

        implicit def implicits[T <: String: ValueOf] = {
            Applyable.implicits[matches[T]]( new matches[T] )
        }
    }

    object required extends ( trim.type && not[empty.type] )

    object rtrim extends Transformation.With[String, String](
        _.replaceFirst( "\\s*$", "" )
    )

    object toLower extends Transformation.With[String, String]( _.toLowerCase )

    object toUpper extends Transformation.With[String, String]( _.toUpperCase )

    object trim extends Transformation.With[String, String]( _.trim )
}

object string extends string