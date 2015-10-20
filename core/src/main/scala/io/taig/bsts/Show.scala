package io.taig.bsts

/**
 * Type class to create the error message for a failed Rule, based on cats.Show
 */
trait Show[-R <: Rule] {
    def show( value: R#Value, rule: R ): String
}

object Show {
    def apply[R <: Rule]( f: ( R#Value, R ) ⇒ String ): Show[R] = new Show[R] {

        override def show( value: R#Value, rule: R ) = f( value, rule )
    }

    def apply[R <: Rule]( f: R#Value ⇒ String ): Show[R] = new Show[R] {

        override def show( value: R#Value, rule: R ) = f( value )
    }

    def apply[R <: Rule]( message: String ): Show[R] = new Show[R] {
        override def show( value: R#Value, rule: R ) = message
    }
}

package default {
    package object show {
        import io.taig.bsts.rule._

        implicit val `Show[Email]` = Show[Email]( "email" )

        implicit val `Show[Exactly]` = Show[Exactly[_]]( "exactly" )

        implicit val `Show[Max]` = Show[Max[_]]( "max" )

        implicit val `Show[Matches]` = Show[Matches[_]]( "matches" )

        implicit val `Show[Min]` = Show[Min[_]]( "min" )

        implicit val `Show[Phone]` = Show[Phone]( "phone" )

        implicit val `Show[Required]` = Show[Required[_]]( "required" )
    }
}