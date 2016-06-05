package io.taig.gandalf.operator

import io.taig.gandalf._

case class LazyAnd[L <: Rule, R <: Rule.Aux[L#Input]]( left: L, right: R ) extends Operation[L, R] {
    override type Input = left.Input

    override type Output = R#Output

    override def apply( input: Input ) = left.verify( input ) andThen right.verify

    override def toString = s"($left && $right)"
}