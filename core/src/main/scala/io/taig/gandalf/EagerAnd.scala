package io.taig.gandalf

import cats.std.list._
import cats.syntax.cartesian._

case class EagerAnd[L <: Rule, R <: Rule.Aux[L#Input]](
    left:  Evaluation[L],
    right: Evaluation[R]
) extends Operation[L, R]

object EagerAnd {
    implicit def evaluation[L <: Rule, R <: Rule.Aux[L#Output]](
        implicit
        lev: Evaluation[L],
        ler: Error[L],
        rev: Evaluation[R],
        rer: Error[R],
        e:   Error[L EagerAnd R]
    ) = {
        Evaluation.instance[L EagerAnd R] { input ⇒
            ( lev.validate( input ) |@| rev.validate( input ) )
                .map { case ( _, _ ) ⇒ input }
                .leftMap( e.error.map( List( _ ) ).getOrElse( _ ) )
        }
    }
}