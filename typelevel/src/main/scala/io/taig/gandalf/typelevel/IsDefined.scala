package io.taig.gandalf.typelevel

import cats.data.Validated._

trait IsDefined[T] extends Mutation {
    override type Input = Option[T]

    override type Output = T
}

object IsDefined {
    implicit def evaluation[T] = Evaluation.instance[IsDefined[T]] {
        case Some( value ) ⇒ valid( value )
        case None          ⇒ invalidNel( "isDefined" )
    }

    implicit val error = Error.instance[IsDefined[_]]( "isDefined" )
}