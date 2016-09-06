package io.taig.gandalf

import cats.data.NonEmptyList

trait Operator extends Rule with Reportable.With[NonEmptyList[String]] {
    type Left <: Rule

    type Right <: Rule.Input[Left#Output]

    override type Input = Left#Input

    override type Output = Right#Output
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

    implicit def error[O <: Operator]: Error[O] = new Error[O] {
        override def show( input: ( O#Input, NonEmptyList[String] ) ) = {
            input._2
        }
    }

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
            ( input, left concat right )
        }
    }
}