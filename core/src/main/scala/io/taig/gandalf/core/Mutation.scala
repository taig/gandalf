package io.taig.gandalf.core

import cats.data.Validated._

trait Mutation extends Rule.Applyable {
    override final def apply( input: Input )(
        implicit
        e: Error[this.type]
    ): Result[this.type] = mutate( input ) match {
        case Some( output ) ⇒ valid( output )
        case None           ⇒ invalid( e.show( arguments( input ) ) )
    }

    def mutate( input: Input ): Option[Output]
}

object Mutation {
    type Input[I] = Mutation { type Input = I }

    type Output[O] = Mutation { type Output = O }

    type Aux[I, O] = Mutation { type Input = I; type Output = O }

    abstract class With[I, O]( f: I ⇒ Option[O] ) extends Mutation {
        override final type Input = I

        override final type Output = O

        override final def mutate( input: I ) = f( input )
    }
}