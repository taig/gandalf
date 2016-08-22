package io.taig.gandalf

import shapeless.Witness

trait Validatable {
    type Input

    type Output

    type Arguments
}

object Validatable {
    type Input[I] = Validatable { type Input = I }

    type Output[O] = Validatable { type Output = O }

    type Aux[I, O] = Validatable { type Input = I; type Output = O }
}

trait Applyable extends Validatable {
    def apply( input: Input )( implicit e: Error[this.type] ): Result[Output]

    def arguments( input: Input ): Arguments
}

object Applyable {
    implicit def validation[A <: Applyable](
        implicit
        w: Witness.Aux[A],
        e: Error[A]
    ): Validation[A] = new Validation[A] {
        override def validate( input: A#Input ) = {
            w.value.apply( input.asInstanceOf[w.value.Input] )
        }
    }
}

trait Input[I] { this: Validatable ⇒
    override type Input = I
}

trait Output[O] { this: Validatable ⇒
    override type Output = O
}

trait Symmetric { this: Validatable ⇒
    override type Output = Input
}

object Arguments {
    trait Input { this: Validatable ⇒
        override final type Arguments = Input
    }
}