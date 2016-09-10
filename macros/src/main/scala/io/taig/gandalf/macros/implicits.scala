package io.taig.gandalf.macros

import io.taig.gandalf.core.{ Rule, Validation }

import scala.language.experimental.macros
import scala.language.implicitConversions

trait implicits {
    implicit def valueToObey[I, R <: Rule.Input[I]]( input: I )(
        implicit
        v: Validation[R]
    ): I Obey R = macro lift.implementation[I, R]
}

object implicits extends implicits