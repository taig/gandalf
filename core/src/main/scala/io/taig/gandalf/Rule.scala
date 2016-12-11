package io.taig.gandalf

trait Rule

object Rule {
    sealed trait Entity extends Rule
    sealed trait Unsafe extends Entity
    sealed trait Transformation extends Entity

    trait Condition extends Unsafe
    trait Mutation extends Unsafe with Transformation
    trait Transition extends Transformation
}