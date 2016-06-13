package io.taig.gandalf.data

import io.taig.gandalf._
import io.taig.gandalf.syntax.aliases._

class LazyAnd extends Operation {
    override type Left <: Rule

    override type Right <: Rule.Aux[Left#Output]
}

object LazyAnd {
    type Aux[L <: Rule, R <: Rule.Aux[L#Output]] = LazyAnd { type Left = L; type Right = R }

    implicit def validation[T, LA <: LazyAnd { type Left <: Rule.Aux[T]; type Right <: Rule.Aux[T] }](
        implicit
        l: Validation[T, LA#Left],
        r: Validation[T, LA#Right],
        e: Error[LA]
    ) = {
        Validation.operation[T, LA]( l.validate( _ ) andThen r.validate ) {
            Error.forward[LA]( _, _ )
        }
    }
}