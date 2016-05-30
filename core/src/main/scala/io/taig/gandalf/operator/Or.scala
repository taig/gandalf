package io.taig.gandalf.operator

import cats.data.Validated._
import cats.std.list._
import cats.syntax.semigroup._
import io.taig.gandalf._
import io.taig.gandalf.internal.TypeShow
import io.taig.gandalf.syntax.aliases._
import shapeless._
import shapeless.syntax.singleton._

class Or[L <: Rule, R <: Rule.Aux[L#Input]] extends Operator[L, R]

object Or {
    implicit def evaluation[L <: Rule, R <: Rule.Aux[L#Output]](
        implicit
        lev: Evaluation[L],
        ler: Error[L],
        rev: Evaluation[R],
        rer: Error[R],
        e:   Error[L || R]
    ) = {
        Evaluation.instance[L || R] { input ⇒
            lev.validate( input ) match {
                case valid @ Valid( _ ) ⇒ valid
                case Invalid( errors1 ) ⇒ rev.validate( input ) match {
                    case valid @ Valid( _ ) ⇒ valid
                    case Invalid( errors2 ) ⇒
                        invalid( e.error( "input" ->> input :: "errors" ->> ( errors1 |+| errors2 ) :: HNil ) )
                }
            }
        }
    }

    implicit def show[L <: Rule, R <: Rule.Aux[L#Input]](
        implicit
        l: TypeShow[L],
        r: TypeShow[R]
    ) = TypeShow.instance[L || R]( s"(${l.show} || ${r.show})" )
}