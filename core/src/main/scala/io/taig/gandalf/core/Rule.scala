package io.taig.gandalf.core

trait Rule

object Rule {
    trait Operator extends Rule

    sealed trait Entity extends Rule

    object Entity

    trait Condition extends Entity

    sealed trait Transformation extends Entity

    trait Mutation extends Transformation
    trait Transition extends Transformation
}