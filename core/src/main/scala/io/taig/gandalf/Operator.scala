package io.taig.gandalf

import cats.data._

trait Operator extends Validatable {
    type Left <: Validatable

    type Right <: Validatable.Input[Left#Output]

    override type Input = Left#Input

    override type Output = Right#Output

    override type Arguments = ( Left#Input, NonEmptyList[String] )
}

object Operator {
    type Left[L <: Validatable] = Operator { type Left = L }

    type Right[R <: Validatable] = Operator { type Right = R }

    type Aux[L <: Validatable, R <: Validatable] = Operator { type Left = L; type Right = R }
}