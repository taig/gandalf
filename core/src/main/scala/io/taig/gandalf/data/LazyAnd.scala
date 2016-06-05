package io.taig.gandalf.data

import io.taig.gandalf._
import io.taig.gandalf.syntax.aliases._

class LazyAnd[L <: Rule, R <: Rule.Aux[L#Input]] extends Operation[L, R]

object LazyAnd {
    implicit def validation[T, L <: Rule.Aux[T], R <: Rule.Aux[T]](
        implicit
        l: Validation[T, L],
        r: Validation[T, R]
    ): Validation[T, L && R] = {
        Validation.instance { input ⇒
            l.validate( input ) andThen r.validate
        }
    }
}