package io.taig.gandalf.operator

import io.taig.gandalf.syntax.aliases._
import io.taig.gandalf.{ Action, Arguments, Error }

abstract class Operation[L <: Action, R <: Action.In[L#Output]]
        extends Action
        with Arguments {
    //    override final type Input = L#Input
    //
    //    override final type Output = R#Output

    override final type Arguments = Error.Forward[this.type]

    def apply( input: Input ): Result[Output]
}

object Operation {
    //    implicit def error[L <: Rule, R <: Rule.Aux[L#Input]]: Error[L Operation R] = new Error[L Operation R] {
    //        override def error( arguments: Error.Forward[L Operation R] ) = arguments( "errors" )
    //    }
}