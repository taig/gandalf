package io.taig.gandalf

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

trait Input[I] { this: Validatable ⇒
    override final type Input = I
}

trait Output[O] { this: Validatable ⇒
    override final type Output = O
}

trait Symmetric { this: Validatable ⇒
    override final type Output = Input
}

object Symmetric {
    trait With[T] extends Symmetric { this: Validatable ⇒
        override final type Input = T
    }
}

object Arguments {
    trait Input extends Applyable {
        override final type Arguments = Input

        override final def arguments( input: Input ) = input
    }
}