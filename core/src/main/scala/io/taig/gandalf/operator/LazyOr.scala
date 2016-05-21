package io.taig.gandalf.operator

import cats.data.Validated._
import io.taig.gandalf._
import io.taig.gandalf.internal.TypeShow

class LazyOr[L <: Rule, R <: Rule.Aux[L#Input]] extends Operator[L, R]

object LazyOr {
    implicit def evaluation[L <: Rule, R <: Rule.Aux[L#Output]](
        implicit
        lev: Evaluation[L],
        ler: Error[L],
        rev: Evaluation[R],
        rer: Error[R],
        e:   Error[L LazyOr R]
    ) = {
        Evaluation.instance[L LazyOr R] { input ⇒
            lev.validate( input ) match {
                case valid @ Valid( _ ) ⇒ valid
                case Invalid( errors1 ) ⇒ rev.validate( input ) match {
                    case valid @ Valid( _ ) ⇒ valid
                    case Invalid( errors2 ) ⇒ invalid( e.error.map( List( _ ) ).getOrElse( errors1 ++ errors2 ) )
                }
            }
        }
    }

    implicit def show[L <: Rule, R <: Rule.Aux[L#Input]](
        implicit
        l: TypeShow[L],
        r: TypeShow[R]
    ) = TypeShow.instance[L LazyOr R]( s"${l.show} || ${r.show}" )
}

