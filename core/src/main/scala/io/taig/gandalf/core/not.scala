package io.taig.gandalf.core

import cats.data.Validated.{ Invalid, Valid, _ }
import shapeless._

class not[R <: Rule] extends Rule {
    override type Input = R#Input

    override type Output = R#Output

    override type Arguments = R#Arguments
}

object not extends not0 {
    def apply[R <: Rule]( Rule: R ): not[R] = new not[R]

    implicit def validationOperatorConditionCondition[O <: Operator { type Left <: Condition; type Right <: Condition.Aux[Left#Output] }](
        implicit
        v:  Validation[O],
        le: Error[not[O#Left]],
        la: Arguments[O#Left],
        re: Error[not[O#Right]],
        ra: Arguments[O#Right],
        e:  Error[not[O]]
    ): Validation[not[O]] = Validation.instance[not[O]] { input ⇒
        v.validate( input ) match {
            case Valid( _ ) ⇒
                val left = le.show( la.collect( input ) )
                val right = re.show( ra.collect( input.asInstanceOf[O#Right#Input] ) )
                val errors = left concat right
                invalid( e.show( input :: errors :: HNil ) )
            case Invalid( _ ) ⇒ valid( input )
        }
    }

    implicit def validationNotNot[R <: Rule](
        implicit
        v: Validation[R]
    ): Validation[not[not[R]]] = Validation.instance[not[not[R]]] {
        v.validate
    }

    implicit def validationNotCondition[C <: Condition](
        implicit
        v: Validation[C],
        e: Error[not[C]],
        a: Arguments[C]
    ): Validation[not[C]] = Validation.instance[not[C]] { input ⇒
        v.validate( input ) match {
            case Valid( _ ) ⇒
                val arguments = a.collect( input )
                val errors = e.show( arguments )
                invalid( errors )
            case Invalid( _ ) ⇒ valid( input )
        }
    }
}

trait not0 {
    implicit def validationOperatorConditionRule[O <: Operator { type Left <: Condition; type Right <: Rule.Input[Left#Output] }](
        implicit
        v: Validation[O { type Left = not[O#Left] }],
        e: Error[not[O]]
    ): Validation[not[O]] = Validation.instance[not[O]] { input ⇒
        v.validate( input ) leftMap { errors ⇒
            e.show( input :: errors :: HNil )
        }
    }

    implicit def validationOperatorRuleCondition[O <: Operator { type Left <: Rule; type Right <: Condition.Aux[Left#Output] }](
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