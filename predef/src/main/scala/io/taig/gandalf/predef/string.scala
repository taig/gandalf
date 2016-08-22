package io.taig.gandalf.predef

import io.taig.gandalf._
import io.taig.gandalf.predef.iterable.nonEmpty
import shapeless.Witness

trait string {
    /**
     * Compare strings
     */
    sealed class matches[T <: String]( implicit w: Witness.Aux[T] ) extends Rule with Input[String] {
        override def check( input: Input ) = input == w.value

        override def arguments( input: Input ): Arguments = ( input, w.value )

        override type Arguments = ( String, T )
    }

    object matches {
        implicit def validation[T <: String](
            implicit
            w: Witness.Aux[T]
        ): Validation[matches[T]] = {
            new Validation[matches[T]] {
                override def validate( input: matches[T]#Input ) = {
                    matches[T]( w )( input )
                }
            }
        }

        def apply[T <: String]( value: Witness.Aux[T] ): matches[T] = {
            new matches[T]()( value )
        }
    }

    /**
     * Transform to lower case
     */
    sealed class toLower extends Transformation with Symmetric with Input[String] {
        override def transform( input: Input ) = input.toUpperCase
    }

    object toLower extends toLower

    /**
     * Require stuff
     */
    sealed class required
        //        extends ( trim ~> asIterable ~> nonEmpty[Char] ~> asString[Iterable[Char]] )
        extends ( trim ~> trim )

    object required extends required {
        implicit val error = Error.identifier[required]
    }

    /**
     * Transform to an Iterable
     */
    sealed class asIterable extends Transformation with Input[String] with Output[Iterable[Char]] {
        override def transform( input: String ) = input.to[Iterable]
    }

    object asIterable extends asIterable

    /**
     * Transform to a String
     */
    sealed class asString[T] extends Transformation with Input[T] with Output[String] {
        override def transform( input: Input ) = input match {
            case iterable: Iterable[_] ⇒ iterable.mkString
            case any                   ⇒ any.toString
        }
    }

    object asString {
        @inline
        def apply[T]: asString[T] = new asString[T]

        implicit def validation[T]: Validation[asString[T]] = {
            new Validation[asString[T]] {
                override def validate( input: asString[T]#Input ) = {
                    apply[T]( input )
                }
            }
        }
    }

    /**
     * Transform to upper case
     */
    sealed class toUpper extends Transformation with Symmetric with Input[String] {
        override def transform( input: Input ) = input.toUpperCase
    }

    object toUpper extends toUpper

    /**
     * Trim
     */
    sealed class trim extends Transformation with Symmetric with Input[String] {
        override def transform( input: String ) = input.trim
    }

    object trim extends trim {
        implicit def validation[T <: trim]: Validation[T] = new Validation[T] {
            override def validate( input: trim#Input ) = trim( input )
        }
    }
}

object string extends string