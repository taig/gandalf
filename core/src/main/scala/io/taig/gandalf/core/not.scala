package io.taig.gandalf.core

import shapeless._

class not[C <: Container] extends Container {
    override final type Kind = C#Kind
}

object not extends not0 {
    def apply[C <: Container]( container: C ): not[C] = new not[C]

    implicit def validationNotNot[C <: Container](
        implicit
        v: Validation[C]
    ): Validation[not[not[C]]] = Validation.instance[not[not[C]]] {
        v.validate
    }
}

trait not0 {
    implicit def validationOperatorRuleMutation[O <: Operator { type Left <: Container; type Right <: Container { type Kind <: Mutation.Input[Left#Kind#Output] } }](
        implicit
        v: Validation[O { type Left = not[O#Left] }],
        r: Report[not[O]]
    ): Validation[not[O]] = Validation.instance[not[O]] { input ⇒
        v.validate( input ) leftMap { errors ⇒
            r.show( input :: errors :: HNil )
        }
    }

    implicit def validationOperatorMutationRule[O <: Operator { type Left <: Container { type Kind <: Mutation }; type Right <: Container { type Kind <: Rule.Input[Left#Kind#Output] } }](
        implicit
        v: Validation[O { type Right = not[O#Right] }],
        r: Report[not[O]]
    ): Validation[not[O]] = Validation.instance[not[O]] { input ⇒
        val cast = input.asInstanceOf[( O { type Right = not[O#Right] } )#Input]
        v.validate( cast ) leftMap { errors ⇒
            r.show( input :: errors :: HNil )
        }
    }
}