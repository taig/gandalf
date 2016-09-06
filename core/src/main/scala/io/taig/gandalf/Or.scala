package io.taig.gandalf

import cats.data.Validated._

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
                invalid( left concat right ).leftMap( e.show( input, _ ) )
            case _ ⇒ valid( input )
        }
    }
}

class ||[L <: Condition, R <: Condition.Aux[L#Output]]
    extends Or
    with Operator.Aux[L, R]