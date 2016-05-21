package io.taig.gandalf.operator

import cats.data.Validated._
import io.taig.gandalf._
import io.taig.gandalf.internal.TypeShow

class EagerOr[L <: Rule, R <: Rule.Aux[L#Input]] extends Operator[L, R]

object EagerOr {
    implicit def evaluation[L <: Rule, R <: Rule.Aux[L#Output]](
        implicit
        lev: Evaluation[L],
        ler: Error[L],
        rev: Evaluation[R],
        rer: Error[R],
        e:   Error[L EagerOr R]
    ) = {
        Evaluation.instance[L EagerOr R] { input ⇒
            ( lev.validate( input ), rev.validate( input ) ) match {
                case ( valid @ Valid( _ ), _ ) ⇒ valid
                case ( _, valid @ Valid( _ ) ) ⇒ valid
                case ( Invalid( errors1 ), Invalid( errors2 ) ) ⇒
                    invalid( e.error.map( List( _ ) ).getOrElse( errors1 ++ errors2 ) )
            }
        }
    }

    implicit def show[L <: Rule, R <: Rule.Aux[L#Input]](
        implicit
        l: TypeShow[L],
        r: TypeShow[R]
    ) = TypeShow.instance[L EagerOr R]( s"${l.show} | ${r.show}" )
}