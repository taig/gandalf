package io.taig.gandalf.core

import shapeless._

class not[R <: Rule] extends Rule {
    override type Input = R#Input

    override type Output = R#Output

    override type Arguments = R#Arguments
}

object not extends not0 {
    def apply[R <: Rule]( Rule: R ): not[R] = new not[R]

    implicit def validationNotNot[R <: Rule](
        implicit
        v: Validation[R]
    ): Validation[not[not[R]]] = Validation.instance[not[not[R]]] {
        v.validate
    }
}

trait not0 {
    implicit def validationOperatorConditionMutation[O <: Operator { type Left <: Condition; type Right <: Mutation.Input[Left#Output] }](
        implicit
        v: Validation[O { type Left = not[O#Left] }],
        e: Error[not[O]]
    ): Validation[not[O]] = Validation.instance[not[O]] { input ⇒
        v.validate( input ) leftMap { errors ⇒
            e.show( input :: errors :: HNil )
        }
    }

    implicit def validationOperatorMutationCondition[O <: Operator { type Left <: Mutation; type Right <: Condition.Aux[Left#Output] }](
        implicit
        v: Validation[O { type Right = not[O#Right] }],
        e: Error[not[O]]
    ): Validation[not[O]] = Validation.instance[not[O]] { input ⇒
        val cast = input.asInstanceOf[( O { type Right = not[O#Right] } )#Input]
        v.validate( cast ) leftMap { errors ⇒
            e.show( input :: errors :: HNil )
        }
    }
}