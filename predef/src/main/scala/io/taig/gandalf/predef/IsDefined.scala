package io.taig.gandalf.predef

import cats.data.Validated._
import io.taig.gandalf.{ Error, Mutation, Validation }

final class IsDefined[T] extends Mutation {
    override type Input = Option[T]

    override type Output = T

    override type Arguments = Error.Input[IsDefined[T]]
}

object IsDefined {
    implicit def validation[T]: Validation[T, IsDefined[T]] = Validation.mutation {
        case Some( value ) ⇒ valid( value )
        case None          ⇒ invalidNel( "IsDefined" )
    }

    def apply[T]: IsDefined[T] = new IsDefined[T]
}