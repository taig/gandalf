package io.taig.gandalf.core

import cats.data.Validated._
import shapeless._

class Or extends Operator {
    override type Right <: Rule.Aux[Left#Input, Left#Output]
}

object Or {
    implicit def validation[O <: Or](
        implicit
        l: Validation[O#Left],
        r: Validation[O#Right],
        e: Option[Error[O]]
    ): Validation[O] = Validation.instance[O] { input ⇒
        l.validate( input ) match {
            case valid @ Valid( _ ) ⇒ valid
            case Invalid( left ) ⇒
                r.validate( input.asInstanceOf[O#Right#Input] ) match {
                    case valid @ Valid( _ ) ⇒ valid
                    case Invalid( right ) ⇒
                        invalid( left concat right ).leftMap { errors ⇒
                            e.fold( errors )( _.show( input :: errors :: HNil ) )
                        }
                }
        }
    }

    implicit def validationNot[O <: Or { type Left <: Rule; type Right <: Rule.Aux[Left#Input, Left#Output] }](
        implicit
        v: Validation[EagerAnd { type Left = not[O#Left]; type Right = not[O#Right] }],
        e: Error[not[O]]
    ): Validation[not[O]] = Validation.instance[not[O]] { input ⇒
        v.validate( input ) leftMap { errors ⇒
            e.show( input :: errors :: HNil )
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

class ||[L <: Rule, R <: Rule.Aux[L#Input, L#Output]] extends Or {
    override final type Left = L

    override final type Right = R
}