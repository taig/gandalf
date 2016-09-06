package io.taig.gandalf

import cats.data.NonEmptyList
import io.taig.gandalf.Rule.Arguments

trait Operator extends Rule with Arguments.With[NonEmptyList[String]] {
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

    implicit def error[O <: Operator]: Error[O] = new Error[O] {
        override def show( input: ( O#Input, NonEmptyList[String] ) ) = {
            input._2
        }
    }
}