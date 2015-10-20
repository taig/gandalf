package io.taig.bsts.rule

import io.taig.bsts._

case class Min[T]( length: Int ) extends Size {
    override type Value = T
}

object Min {
    implicit def `Definition[Min[T]]`[T: Length] = Size.Definition[Min[T]]( _ >= _ )
}