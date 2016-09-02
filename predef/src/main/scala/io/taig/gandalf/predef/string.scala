package io.taig.gandalf.predef

import io.taig.gandalf._
import io.taig.gandalf.syntax.all._

import scala.language.higherKinds

/*
 * @Definition
 * sealed class trim extends Transformation[String]( _.trim )
 *
 * @Definition
 * sealed class nonEmpty extends Rule[String]( _.nonEmpty )
 * 
 * @Definition( trim ~> nonEmpty )
 * sealed class required
 * 
 * case class User( @obeys( required ) name: String )
 * 
 * User( "foobar" )
 * // User( "   " )
 */

trait string {
    @Definition
    sealed class isEmpty
        extends Rule.With[String]( _.isEmpty )
        with Arguments.Input

    @Definition
    sealed class nonEmpty
        extends Rule.With[String]( _.nonEmpty )
        with Arguments.Input

    sealed class regex[T <: String: ValueOf]
            extends Rule.With[String]( _ matches valueOf[T] ) {
        override type Arguments = ( String, T )

        override def arguments( input: Input ) = ( input, valueOf[T] )
    }

    sealed class required extends ( trim ~> nonEmpty )

    object required extends required {
        implicit val error = Error.identifier[required]
    }

    @Definition
    sealed class toLower extends Transformation.With[String, String]( _.toLowerCase )

    @Definition
    sealed class toUpper extends Transformation with Symmetric.With[String] {
        override def transform( input: Input ) = input.toUpperCase
    }

    @Definition
    sealed class trim extends Transformation with Symmetric.With[String] {
        override def transform( input: String ) = input.trim
    }
}

object string extends string