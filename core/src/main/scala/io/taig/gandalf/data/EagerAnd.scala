package io.taig.gandalf.data

import cats.std.list._
import cats.syntax.cartesian._
import io.taig.gandalf._
import io.taig.gandalf.syntax.aliases._

class EagerAnd[L <: Rule, R <: Rule.Aux[L#Input]] extends Operation[L, R]

object EagerAnd {
    implicit def validation[T, L <: Rule.Aux[T], R <: Rule.Aux[T]](
        implicit
        l: Validation[T, L],
        r: Validation[T, R],
        e: Error[L & R]
    ) = {
        Validation.operation[T, L, R, L & R] { input ⇒
            ( l.validate( input ) |@| r.validate( input ) ) map { case ( _, _ ) ⇒ input }
        } {
            Error.forward[L & R]( _, _ )
        }
    }
}