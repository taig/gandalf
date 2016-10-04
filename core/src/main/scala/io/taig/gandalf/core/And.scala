package io.taig.gandalf.core

import shapeless._

trait And extends Operator

object And {
    implicit def validationNot[A <: And { type Left <: Rule; type Right <: Condition.Aux[Left#Output] }](
        implicit
        v: Validation[Or { type Left = not[A#Left]; type Right = not[A#Right] }],
        e: Error[not[A]]
    ): Validation[not[A]] = Validation.instance[not[A]] { input ⇒
        v.validate( input ) leftMap { errors ⇒
            e.show( input :: errors :: HNil )
        }
    }
}