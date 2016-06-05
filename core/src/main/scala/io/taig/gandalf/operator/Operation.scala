package io.taig.gandalf.operator

import io.taig.gandalf.syntax.aliases._
import io.taig.gandalf.{ Action, Arguments, Error }

abstract class Operation[L <: Action, R <: Action.In[L#Output]]
        extends Action
        with Arguments {
    override final type Arguments = Error.Forward[this.type]

    override def validate( input: Input ) = apply( input )

    def apply( input: Input ): Result[Output]
}