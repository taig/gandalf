package io.taig.gandalf

import cats.data.Validated._

trait Mutation extends Rule

object Mutation {
    type Input[I] = Mutation { type Input = I }

    type Output[O] = Mutation { type Output = O }

    type Aux[I, O] = Mutation { type Input = I; type Output = O }

    trait Applyable[I, O]
            extends Mutation
            with Rule.Applyable {
        override type Input = I

        override type Output = O

        override final def apply( input: I )(
            implicit
            e: Error[this.type]
        ) = mutate( input ) match {
            case Some( output ) ⇒ valid( output )
            case None           ⇒ invalid( e.show( arguments( input ) ) )
        }

        def mutate( input: I ): Option[O]
    }

    abstract class With[I, O]( f: I ⇒ Option[O], args: Any* )
            extends Applyable[I, O] {
        override def mutate( input: I ) = f( input )
    }
}