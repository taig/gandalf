package io.taig.gandalf.data

import io.taig.gandalf.Arguments

trait Rule extends Action with Arguments {
    override final type Output = Input
}

object Rule {
    type Aux[T] = Rule { type Input = T }
}