package io.taig.gandalf.core

import cats.data.Validated._
import shapeless.HNil

class Or extends Operator.Logical

object Or {
    implicit def validation[O <: Or](
        implicit
        l: Validation[O#Left],
        r: Validation[O#Right],
        e: Error[O]
    ): Validation[O] = Validation.instance[O] { input ⇒
        val left = l.validate( input )
        val right = r.validate( input.asInstanceOf[O#Right#Input] )
        ( left, right ) match {
            case ( Invalid( left ), Invalid( right ) ) ⇒
                invalid( left concat right ).leftMap { errors ⇒
                    e.show( input :: errors :: HNil )
                }
            case _ ⇒ valid( input )
        }
    }

    implicit def serialization[O <: Or](
        implicit
        l: Serialization[O#Left],
        r: Serialization[O#Right]
    ): Serialization[O] = {
        Serialization.instance( s"(${l.serialize} || ${r.serialize})" )
    }
}

class ||[L <: Condition, R <: Condition.Aux[L#Output]]
    extends Or
    with Operator.Aux[L, R]