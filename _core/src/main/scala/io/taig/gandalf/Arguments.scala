package io.taig.gandalf

import shapeless.HList

trait Arguments {
    type Arguments <: HList
}

object Arguments {
    type Aux[A <: HList] = Reportable { type Arguments = A }
}