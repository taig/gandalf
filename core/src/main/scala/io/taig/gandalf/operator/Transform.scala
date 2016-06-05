package io.taig.gandalf.operator

import io.taig.gandalf._
import io.taig.gandalf.syntax.aliases._

class Transform[L <: Transformation, R <: Action.In[L#Output]] extends Operation[L, R] {
    override def apply( input: Input ) = ???
}