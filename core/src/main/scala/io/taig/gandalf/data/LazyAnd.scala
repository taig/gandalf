package io.taig.gandalf.data

import io.taig.gandalf._
import io.taig.gandalf.syntax.aliases._

class LazyAnd[L <: Rule, R <: Rule.Aux[L#Input]] extends Operation[L, R]

object LazyAnd {
    implicit def validation[T, L <: Rule.Aux[T], R <: Rule.Aux[T]](
        implicit
        l: Validation[T, L],
        r: Validation[T, R],
        e: Error[L && R]
    ) = {
        Validation.operation[T, L, R, L && R]( l.validate( _ ) andThen r.validate ) {
            Error.forward[L && R]( _, _ )
        }
    }
}