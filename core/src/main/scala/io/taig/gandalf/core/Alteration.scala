package io.taig.gandalf.core

trait Alteration extends Rule

object Alteration {
    type Input[I] = Alteration { type Input = I }

    type Output[O] = Alteration { type Output = O }

    type Aux[I, O] = Alteration { type Input = I; type Output = O }
}