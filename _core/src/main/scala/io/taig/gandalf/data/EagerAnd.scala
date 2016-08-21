package io.taig.gandalf.data

import cats.std.list._
import cats.syntax.cartesian._
import io.taig.gandalf._

class EagerAnd extends Operator {
    override type Left <: Rule

    override type Right <: Rule.Aux[Left#Output]
}

object EagerAnd {
    type Aux[L <: Rule, R <: Rule.Aux[L#Output]] = EagerAnd {type Left = L; type Right = R }

    implicit def validation[T, EA <: EagerAnd { type Left <: Rule.Aux[T]; type Right <: Rule.Aux[T] }](
        implicit
        l: Validation[T, EA#Left],
        r: Validation[T, EA#Right],
        e: Error[EA]
    ) = {
        Validation.operation[T, EA] { input ⇒
            ( l.validate( input ) |@| r.validate( input ) ) map { case ( _, _ ) ⇒ input }
        } {
            Error.forward[EA]( _, _ )
        }
    }
}