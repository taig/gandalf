package io.taig.gandalf

class Mutate extends Operator with Mutation {
    override type Left <: Mutation

    override type Right <: Validatable.Input[Left#Output]
}

object Mutate {
    implicit def validation[M <: Mutate](
        implicit
        vl: Validation[M#Left],
        vr: Validation[M#Right],
        e:  Error[M]
    ): Validation[M] = new Validation[M] {
        override def validate( input: M#Input ) = {
            //            vl.validate( input ).andThen { output ⇒
            //                vr.validate( output.asInstanceOf[M#Right#Input] )
            //            }.leftMap( e.show( input, _ ) )
            ???
        }
    }
}