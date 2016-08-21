package io.taig.gandalf

import cats.data.Validated._
import cats.std.list._
import cats.syntax.semigroup._

trait Or extends Operator with Rule {
    override type Left <: Rule

    override type Right <: Rule.Aux[Left#Output]
}

object Or {
    type Aux[L <: Rule, R <: Rule.Aux[L#Output]] = LazyAnd {
        type Left = L

        type Right = R
    }

    implicit def validation[LA <: LazyAnd](
        implicit
        l: Validation[LA#Left],
        r: Validation[LA#Right],
        e: Error[LA]
    ): Validation[LA] = Validation.operation[LA] { input ⇒
        l.validate( input ) match {
            case Valid( output ) ⇒ valid( output )
            case Invalid( errorsLeft ) ⇒
                r.validate( input.asInstanceOf[LA#Right#Input] ) match {
                    case Valid( output ) ⇒ valid( output )
                    case Invalid( errorsRight ) ⇒
                        invalid( errorsLeft |+| errorsRight )
                }
        }
    } { ( _, _ ) }
}