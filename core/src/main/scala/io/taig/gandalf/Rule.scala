package io.taig.gandalf

trait Rule extends Validatable with Symmetric

object Rule {
    type Aux[T] = Rule { type Input = T }
}