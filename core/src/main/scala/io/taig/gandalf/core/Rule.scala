package io.taig.gandalf.core

trait Rule {
    type Out <: Rule
}

object Rule {
    trait Operator[L <: Rule, R <: Rule] extends Rule

    trait Entity extends Rule {
        override final type Out = this.type
    }

    object Entity {
        trait Condition extends Entity
        trait Mutation extends Entity
        trait Transformation extends Entity
    }
}