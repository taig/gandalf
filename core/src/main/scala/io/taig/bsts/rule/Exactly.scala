package io.taig.bsts.rule

import io.taig.bsts._

case class Exactly[T]( length: Int ) extends Size {
    override type Value = T
}

object Exactly {
    implicit def `Definition[Exactly[T]]`[T: Length] = Size.Definition[Exactly[T]]( _ == _ )
}