package io.taig.gandalf.operator

import io.taig.gandalf._

case class Mutate[L <: Mutation, R <: Action.In[L#Output]]( left: L, right: R )(
        implicit
        v: Validation[R]
) extends Operation[L, R] {
    override type Input = left.Input

    override type Output = R#Output

    override def apply( input: Input ) = left.mutate( input ) andThen v.validate( right )

    override def toString = s"$left <*> $right"
}