package io.taig.gandalf

class Mutate
        extends Operator
        with Mutation {
    override type Left <: Mutation

    override type Right <: Rule.Input[Left#Output]
}

object Mutate {
    implicit def validation[M <: Mutate](
        implicit
        l: Validation[M#Left],
        r: Validation[M#Right],
        e: Error[M]
    ): Validation[M] = Validation.instance[M] { input ⇒
        l.validate( input ) andThen { output ⇒
            r.validate( output.asInstanceOf[M#Right#Input] )
        } leftMap { e.show( input, _ ) }
    }
}

class ~>[L <: Mutation, R <: Rule.Input[L#Output]]
    extends Mutate
    with Operator.Aux[L, R]