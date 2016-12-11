package io.taig.gandalf.macros

import io.taig.gandalf._

import scala.language.experimental.macros
import scala.language.implicitConversions

class Obey[R <: Rule, I, O]( val value: O ) extends AnyVal with Serializable {
    override def toString: String = value.toString
}

object Obey {
    implicit def valueToObey[R <: Rule, I, O]( input: I )(
        implicit
        v: Validation[R, I, O],
        s: Serialization[R]
    ): Obey[R, I, O] = macro lift.implementation[R, I, O]

    implicit def obeyToValue[R <: Rule, I, O]( obey: Obey[R, I, O] ): O =
        obey.value

    def applyUnsafe[R <: Rule, I, O]( value: O ): Obey[R, I, O] =
        new Obey[R, I, O]( value )

    def unapply[R <: Rule, I, O](
        obeys: Obey[R, I, O]
    ): Option[O] = Some( obeys.value )
}