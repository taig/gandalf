package io.taig.gandalf.core

import shapeless._

trait Rule {
    type Input

    type Output
}

object Rule {
    type Input[I] = Rule { type Input = I }

    type Output[O] = Rule { type Output = O }

    type Aux[I, O] = Rule { type Input = I; type Output = O }

    trait Applyable extends Rule with Reportable {
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

        implicit def arguments[A <: Applyable](
            implicit
            w: Witness.Aux[A]
        ): Arguments[A] = Arguments.instance { input ⇒
            w.value.arguments( input.asInstanceOf[w.value.Input] )
        }

        def implicits[A <: Applyable]( f: ⇒ A )(
            implicit
            e: Error[A]
        ): Validation[A] with Arguments[A] = {
            val applyable = f

            new Validation[A] with Arguments[A] {
                override def validate( input: A#Input ) = {
                    applyable.apply( input.asInstanceOf[applyable.Input] )
                }

                override def collect( input: A#Input ) = {
                    applyable.arguments( input.asInstanceOf[applyable.Input] )
                }
            }
        }
    }
}