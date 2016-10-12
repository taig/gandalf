package io.taig.gandalf.core

import shapeless._

class LazyAnd extends And {
    override type Right <: Container { type Kind <: Rule.Input[Left#Kind#Output] }
}

object LazyAnd {
    implicit def validation[A <: LazyAnd](
        implicit
        l: Validation[A#Left],
        r: Lazy[Validation[A#Right]],
        e: Option[Error[A]]
    ): Validation[A] = Validation.instance[A] { input ⇒
        l.validate( input ) andThen { output ⇒
            r.value.validate( output.asInstanceOf[A#Right#Kind#Input] )
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

class &&[L <: Container, R <: Container { type Kind <: Rule.Input[L#Kind#Output] }] extends LazyAnd {
    override final type Left = L

    override final type Right = R
}