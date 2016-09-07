package io.taig.gandalf.core

trait Alter
        extends Operator
        with Alteration {
    override type Left <: Alteration

    override type Right <: Rule.Input[Left#Output]
}

object Alter {
    implicit def validation[A <: Alter](
        implicit
        l: Validation[A#Left],
        r: Validation[A#Right],
        e: Error[A]
    ): Validation[A] = Validation.instance[A] { input ⇒
        l.validate( input ) andThen { output ⇒
            r.validate( output.asInstanceOf[A#Right#Input] )
        } leftMap { e.show( input, _ ) }
    }
}

class ~>[L <: Alteration, R <: Rule.Input[L#Output]]
    extends Alter
    with Operator.Aux[L, R]