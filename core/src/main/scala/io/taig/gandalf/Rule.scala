package io.taig.gandalf

import io.taig.gandalf.syntax.aliases._

trait Rule extends Action with Arguments {
    override final type Output = Input

    override def validate( input: Input ) = verify( input )

    def verify( input: Input ): Result[Output]
}

object Rule {
    type Aux[T] = Rule { type Input = T }
}