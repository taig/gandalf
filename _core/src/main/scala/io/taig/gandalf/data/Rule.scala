package io.taig.gandalf.data

import io.taig.gandalf.Reportable$

trait Rule extends Action with Reportable {
    override final type Output = Input
}

object Rule {
    type Aux[T] = Rule { type Input = T }
}