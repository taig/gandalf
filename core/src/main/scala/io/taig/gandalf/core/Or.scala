package io.taig.gandalf.core

import cats.data.Validated._
import shapeless._

class Or extends Operator {
    override type Right <: Container { type Kind <: Rule.Aux[Left#Kind#Input, Left#Kind#Output] }
}

object Or {
    implicit def validation[O <: Or](
        implicit
        l: Validation[O#Left],
        r: Lazy[Validation[O#Right]],
        e: Option[Report[O]]
    ): Validation[O] = Validation.instance[O] { input ⇒
        l.validate( input ) match {
            case valid @ Valid( _ ) ⇒ valid
            case Invalid( left ) ⇒
                r.value.validate( input.asInstanceOf[O#Right#Kind#Input] ) match {
                    case valid @ Valid( _ ) ⇒ valid
                    case Invalid( right ) ⇒
                        invalid( left concat right ).leftMap { errors ⇒
                            e.fold( errors )( _.show( input :: errors :: HNil ) )
                        }
                }
        }
    }

    implicit def validationNot[O <: Or { type Left <: Container; type Right <: Container { type Kind <: Rule.Aux[Left#Kind#Input, Left#Kind#Output] } }](
        implicit
        v: Validation[EagerAnd { type Left = not[O#Left]; type Right = not[O#Right] }],
        r: Report[not[O]]
    ): Validation[not[O]] = Validation.instance[not[O]] { input ⇒
        v.validate( input ) leftMap { errors ⇒
            r.show( input :: errors :: HNil )
        }
    }

    implicit def serialization[O <: Or](
        implicit
        l: Serialization[O#Left],
        r: Serialization[O#Right]
    ): Serialization[O] = {
        Serialization.instance( s"(${l.serialize} || ${r.serialize})" )
    }
}

class ||[L <: Container, R <: Container { type Kind <: Rule.Aux[L#Kind#Input, L#Kind#Output] }] extends Or {
    override final type Left = L

    override final type Right = R
}