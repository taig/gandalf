package io.taig.gandalf.core

import cats.syntax.cartesian._
import shapeless._

class EagerAnd extends And {
    override type Right <: Rule.Aux[Left#Input, Left#Output]
}

object EagerAnd {
    implicit def validation[A <: EagerAnd](
        implicit
        l: Validation[A#Left],
        r: Validation[A#Right],
        e: Option[Error[A]]
    ): Validation[A] = Validation.instance[A] { input ⇒
        val left = l.validate( input )
        val right = r.validate( input.asInstanceOf[A#Right#Input] )

        ( left |@| right )
            .map( ( _, output ) ⇒ output )
            .leftMap { errors ⇒
                e.fold( errors )( _.show( input :: errors :: HNil ) )
            }
    }

    implicit def serialization[A <: EagerAnd](
        implicit
        l: Serialization[A#Left],
        r: Serialization[A#Right]
    ): Serialization[A] = {
        Serialization.instance( s"(${l.serialize} & ${r.serialize})" )
    }
}

class &[L <: Rule, R <: Rule.Aux[L#Input, L#Output]] extends EagerAnd {
    override final type Left = L

    override final type Right = R
}