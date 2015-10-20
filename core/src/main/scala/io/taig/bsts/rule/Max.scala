package io.taig.bsts.rule

import io.taig.bsts._

case class Max[T]( length: Int ) extends Size {
    override type Value = T
}

object Max {
    implicit def `Definition[Max[T]]`[T: Length] = Size.Definition[Max[T]]( _ <= _ )
}