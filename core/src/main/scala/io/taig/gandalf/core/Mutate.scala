package io.taig.gandalf.core

import shapeless._

trait Mutate
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
        } leftMap { errors ⇒
            e.show( input :: errors :: HNil )
        }
    }

    implicit def serialization[M <: Mutate](
        implicit
        l: Serialization[M#Left],
        r: Serialization[M#Right]
    ): Serialization[M] = {
        Serialization.instance( s"(${l.serialize} ~> ${r.serialize})" )
    }
}

class ~>[L <: Mutation, R <: Rule.Input[L#Output]]
    extends Mutate
    with Operator.Aux[L, R]