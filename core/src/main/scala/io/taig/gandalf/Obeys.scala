package io.taig.gandalf

import scala.language.implicitConversions

class Obeys[R <: Rule, I, O]( val value: O ) extends AnyVal with Serializable {
    override def toString = value.toString
}

object Obeys {
    implicit def obeysToValue[R <: Rule, I, O]( obeys: Obeys[R, I, O] ): O =
        obeys.value

    def applyUnsafe[R <: Rule, I, O]( value: O ): Obeys[R, I, O] =
        new Obeys[R, I, O]( value )

    def unapply[R <: Rule, I, O]( obeys: Obeys[R, I, O] ): Option[O] =
        Some( obeys.value )
}