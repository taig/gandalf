package io.taig.gandalf.core

class not[R <: Rule] extends Rule

object not {
    @inline
    def apply[R <: Rule]( rule: R ): not[R] = new not[R]

    implicit def condition[C <: Rule.Condition, T](
        implicit
        v: Validation[C, T, T]
    ): Validation[not[C], T, T] = Validation.instance { input ⇒
        v( input ) match {
            case Some( _ ) ⇒ None
            case None      ⇒ Some( input )
        }
    }

    implicit def serialization[R <: Rule](
        implicit
        s: Serialization[R]
    ): Serialization[not[R]] = Serialization.instance( s"not($s)" )
}