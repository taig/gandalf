package io.taig.bsts.rule

import io.taig.bsts._
import scala.language.existentials

abstract class Size extends Rule {
    def length: Int
}

object Size {
    def Definition[R <: Size]( f: ( Int, Int ) ⇒ Boolean )( implicit length: Length[R#Value] ) = new Definition[R] {
        override def apply( value: R#Value, rule: R ) = f( length.length( value ), rule.length )
    }
}