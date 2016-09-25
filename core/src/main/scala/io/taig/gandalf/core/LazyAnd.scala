package io.taig.gandalf.core

import shapeless._

class LazyAnd extends Operator {
    override type Right <: Rule.Input[Left#Output]
}

object LazyAnd {
    implicit def validation[A <: LazyAnd](
        implicit
        l: Validation[A#Left],
        r: Validation[A#Right],
        e: Option[Error[A]]
    ): Validation[A] = Validation.instance[A] { input ⇒
        l.validate( input ) andThen { output ⇒
            r.validate( output.asInstanceOf[A#Right#Input] )
        } leftMap { errors ⇒
            e.fold( errors )( _.show( input :: errors :: HNil ) )
        }
    }

    implicit def serialization[A <: LazyAnd](
        implicit
        l: Serialization[A#Left],
        r: Serialization[A#Right]
    ): Serialization[A] = {
        Serialization.instance( s"(${l.serialize} && ${r.serialize})" )
    }
}

class &&[L <: Rule, R <: Rule.Input[L#Output]] extends LazyAnd {
    override final type Left = L

    override final type Right = R
}