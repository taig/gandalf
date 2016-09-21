package io.taig.gandalf.core

import cats.data.NonEmptyList
import cats.data.Validated._
import shapeless._

trait Operator extends Rule {
    type Left <: Rule

    type Right <: Rule

    override type Input = Left#Input

    override type Output = Right#Output

    override final type Arguments = Input :: NonEmptyList[String] :: HNil
}

object Operator {
    implicit def error[O <: Operator]: Error[O] = {
        Error.instance[O]( _.at( 1 ) )
    }

    implicit def errorNot[O <: Operator]: Error[not[O]] = {
        Error.instance[not[O]]( _.at( 1 ) )
    }

    implicit def negationConditionCondition[A <: Operator { type Left <: Condition; type Right <: Condition.Aux[Left#Output] }](
        implicit
        v:  Validation[A],
        le: Error[not[A#Left]],
        la: Arguments[A#Left],
        re: Error[not[A#Right]],
        ra: Arguments[A#Right],
        e:  Error[not[A]]
    ): Negation[A] = Negation.instance[A] { input ⇒
        v.validate( input ) match {
            case Valid( _ ) ⇒
                val left = le.show( la.collect( input ) )
                val right = re.show( ra.collect( input.asInstanceOf[A#Right#Input] ) )
                invalid( e.show( input :: ( left concat right ) :: HNil ) )
            case Invalid( _ ) ⇒ valid( input )
        }
    }

    implicit def negationConditionMutation[A <: Operator { type Left <: Condition; type Right <: Mutation.Input[Left#Output] }](
        implicit
        v: Validation[A { type Left = not[A#Left] }],
        e: Error[not[A]]
    ): Negation[A] = Negation.instance[A] { input ⇒
        v.validate( input ) leftMap { errors ⇒
            e.show( input :: errors :: HNil )
        }
    }

    implicit def negationMutationCondition[A <: Operator { type Left <: Mutation; type Right <: Condition.Aux[Left#Output] }](
        implicit
        v: Validation[A { type Right = not[A#Right] }],
        e: Error[not[A]]
    ): Negation[A] = Negation.instance { input ⇒
        val cast = input.asInstanceOf[( A { type Right = not[A#Right] } )#Input]
        v.validate( cast ) leftMap { errors ⇒
            e.show( input :: errors :: HNil )
        }
    }
}
