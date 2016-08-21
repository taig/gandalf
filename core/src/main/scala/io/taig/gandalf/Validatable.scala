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