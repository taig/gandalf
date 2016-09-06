package io.taig.gandalf

import shapeless._

trait Rule {
    type Input

    type Output
}

object Rule {
    type Input[I] = Rule { type Input = I }

    type Output[O] = Rule { type Output = O }

    type Aux[I, O] = Rule { type Input = I; type Output = O }

    trait Applyable extends Rule with Arguments {
        def apply( input: Input )(
            implicit
            e: Error[this.type]
        ): Result[this.type]

        def arguments( input: Input ): Arguments
    }

    object Applyable {
        implicit def validation[A <: Applyable](
            implicit
            w: Witness.Aux[A],
            e: Error[A]
        ): Validation[A] = Validation.instance[A] { input ⇒
            w.value.apply(
                input.asInstanceOf[w.value.Input]
            )( e.asInstanceOf[Error[w.value.type]] )
        }
    }

    trait Arguments extends Rule {
        type Arguments
    }

    object Arguments {
        trait With[A] extends Arguments {
            override final type Arguments = ( Input, A )
        }

        trait Input extends Applyable {
            override final type Arguments = Input

            override final def arguments( input: Input ) = input
        }

        trait None extends Applyable {
            override final type Arguments = Unit

            override final def arguments( input: Input ) = {}
        }
    }
}