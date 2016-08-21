package io.taig.gandalf

trait Mutate extends Operator with Mutation {
    override type Left <: Mutation

    override type Right <: Validatable.Input[Left#Output]
}

object Mutate {
    type Aux[L <: Mutation, R <: Validatable.Input[L#Output]] = Mutate {
        type Left = L

        type Right = R
    }

    implicit def validation[M <: Mutate](
        implicit
        l: Validation[M#Left],
        r: Validation[M#Right],
        e: Error[M]
    ): Validation[M] = {
        ???
    }
}