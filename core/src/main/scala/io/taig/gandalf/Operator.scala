package io.taig.gandalf

import cats.data.NonEmptyList

trait Operator extends Validatable {
    type Left <: Validatable

    type Right <: Validatable.Input[Left#Output]

    override type Input = Left#Input

    override type Output = Right#Output

    override type Arguments = ( Input, NonEmptyList[String] )
}