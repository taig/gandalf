package io.taig.gandalf.operator

import cats.std.list._
import cats.syntax.cartesian._
import io.taig.gandalf._

case class EagerAnd[L <: Rule, R <: Rule.Aux[L#Input]]( left: L, right: R ) extends Operation[L, R] {
    override type Input = left.Input

    override type Output = R#Output

    override def apply( input: Input ) = {
        ( left.verify( input ) |@| right.verify( input ) )
            .map { case ( _, _ ) ⇒ input }
    }

    override def toString = s"($left & $right)"
}