package io.taig.gandalf.macros.conversion

import io.taig.gandalf._
import io.taig.gandalf.macros.lift

import scala.language.experimental.macros
import scala.language.implicitConversions

trait obeys {
    implicit def gandalfMacrosValueToObey[R <: Rule, I, O]( input: I )(
        implicit
        v: Validation[R, I, O],
        s: Serialization[R]
    ): Obeys[R, I, O] = macro lift.implementation[R, I, O]
}

object obeys extends obeys