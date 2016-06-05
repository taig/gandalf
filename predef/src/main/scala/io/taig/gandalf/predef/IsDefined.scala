package io.taig.gandalf.predef

import cats.data.Validated._
import io.taig.gandalf.{ Error, Mutation }

import scala.reflect._

case class IsDefined[T]( implicit val tag: ClassTag[T] ) extends Mutation {
    override type Input = Option[T]

    override type Output = T

    override type Arguments = Error.Input[IsDefined[T]]

    override def mutate( input: Option[T] ) = input match {
        case Some( input ) ⇒ valid( input )
        case None          ⇒ invalidNel( "IsDefined" )
    }

    override def equals( that: Any ) = that match {
        case that: IsDefined[_] ⇒ this.tag == that.tag
        case _                  ⇒ false
    }
}