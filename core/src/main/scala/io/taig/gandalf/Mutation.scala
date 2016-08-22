package io.taig.gandalf

import cats.data.Validated._

trait Mutation extends Validatable

object Mutation {
    type Input[I] = Mutation { type Input = I }

    type Output[O] = Mutation { type Output = O }

    type Aux[I, O] = Mutation { type Input = I; type Output = O }

    trait Custom extends Mutation with Applyable {
        override def apply( input: Input )( implicit e: Error[this.type] ) = {
            mutate( input ) match {
                case Some( output ) ⇒ valid( output )
                case None ⇒
                    val errors = e.show( arguments( input ) )
                    invalid( errors )
            }
        }

        def mutate( input: Input ): Option[Output]
    }

    object Custom {
        type Input[I] = Custom { type Input = I }

        type Output[O] = Custom { type Output = O }

        type Aux[I, O] = Custom { type Input = I; type Output = O }
    }
}