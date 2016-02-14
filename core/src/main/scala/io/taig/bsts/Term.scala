package io.taig.bsts

import shapeless._

abstract class Term[N <: String, I, O, A <: HList]( implicit w: Witness.Aux[N] ) extends Validation[I, O] {
    def name: String = w.value

    override def toString = name
}

object Term {
    type Aux[N <: String, I, O, A <: HList, R0] = Term[N, I, O, A] { type R = R0 }
}