package io.taig.gandalf.core

import cats.data.NonEmptyList
import shapeless._

trait Operator extends Rule {
    type Left <: Rule

    type Right <: Rule.Input[Left#Output]

    override type Input = Left#Input

    override type Output = Right#Output

    override final type Arguments = Input :: NonEmptyList[String] :: HNil
}

object Operator {
    trait Aux[L <: Rule, R <: Rule.Input[L#Output]] extends Operator {
        override final type Left = L

        override final type Right = R
    }

    trait Logical extends Operator with Condition {
        override type Left <: Condition

        override type Right <: Condition.Aux[Left#Output]
    }

    implicit def error[O <: Operator]: Error[O] = Error.instance( _.at( 1 ) )

    implicit def arguments[L <: Logical](
        implicit
        le: Error[L#Left],
        la: Arguments[L#Left],
        re: Error[L#Right],
        ra: Arguments[L#Right]
    ): Arguments[L] = new Arguments[L] {
        override def collect( input: L#Input ) = {
            val left = le.show( la.collect( input ) )
            val right = re.show( ra.collect( input.asInstanceOf[L#Right#Input] ) )
            input :: ( left concat right ) :: HNil
        }
    }
}