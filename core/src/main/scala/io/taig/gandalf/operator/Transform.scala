package io.taig.gandalf.operator

import io.taig.gandalf._

case class Transform[L <: Transformation, R <: Action.In[L#Output]]( left: L, right: R ) extends Operation[L, R] {
    override type Input = left.Input

    override type Output = R#Output

    override def apply( input: Input ) = right.validate( left.transform( input ) )
}