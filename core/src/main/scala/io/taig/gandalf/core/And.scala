package io.taig.gandalf.core

import shapeless._

trait And extends Operator

object And {
    implicit def validationNot[A <: And { type Left <: Container; type Right <: Container { type Kind <: Rule.Aux[Left#Kind#Input, Left#Kind#Output] } }](
        implicit
        v: Validation[Or { type Left = not[A#Left]; type Right = not[A#Right] }],
        r: Report[not[A]]
    ): Validation[not[A]] = Validation.instance[not[A]] { input ⇒
        v.validate( input ) leftMap { errors ⇒
            r.show( input :: errors :: HNil )
        }
    }
}