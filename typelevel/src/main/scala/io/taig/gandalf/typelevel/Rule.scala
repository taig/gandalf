package io.taig.gandalf.typelevel

trait Rule extends Validation {
    override final type Output = Input
}

object Rule {
    type Aux[T] = Rule { type Input = T }
}