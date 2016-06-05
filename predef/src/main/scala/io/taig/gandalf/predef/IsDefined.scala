package io.taig.gandalf.predef

import cats.data.Validated._
import io.taig.gandalf.{ Error, Mutation }

class IsDefined[T] extends Mutation {
    override type Input = Option[T]

    override type Output = T

    override type Arguments = Error.Input[IsDefined[T]]

    override def mutate( input: Option[T] ) = input match {
        case Some( input ) ⇒ valid( input )
        case None          ⇒ invalidNel( "IsDefined" )
    }
}

object IsDefined {
    def isDefined[T]: IsDefined[T] = new IsDefined[T]
}