package io.taig.gandalf.operator

import io.taig.gandalf._

case class Mutate[L <: Mutation, R <: Action.In[L#Output]]( left: L, right: R ) extends Operation[L, R] {
    override type Input = left.Input

    override type Output = R#Output

    override def apply( input: Input ) = left.validate( input ) andThen right.validate

    override def toString = s"$left <*> $right"
}