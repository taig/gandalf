package io.taig.bsts.rule

import io.taig.bsts._

import scala.language.postfixOps

case class Required[T]() extends Rule {
    type Value = T
}

object Required {
    implicit def `Definition[Required[T]]`[T]( implicit empty: Empty[Rule.Aux[T]] ) = {
        Definition[Required[T]]( ( value: T ) ⇒ !empty.isEmpty( value ) )
    }
}