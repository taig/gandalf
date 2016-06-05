package io.taig.gandalf.operator

import cats.data.Validated._
import cats.std.list._
import cats.syntax.semigroup._
import io.taig.gandalf._

case class Or[L <: Rule, R <: Rule.Aux[L#Input]]( left: L, right: R ) extends Operation[L, R] {
    override type Input = left.Input

    override type Output = R#Output

    override def apply( input: Input ) = left.verify( input ) match {
        case valid @ Valid( _ ) ⇒ valid
        case Invalid( errors1 ) ⇒ right.verify( input ) match {
            case valid @ Valid( _ ) ⇒ valid
            case Invalid( errors2 ) ⇒ invalid( errors1 |+| errors2 )
        }
    }

    override def toString = s"($left || $right)"
}