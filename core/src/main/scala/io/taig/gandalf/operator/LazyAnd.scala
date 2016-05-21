package io.taig.gandalf.operator

import io.taig.gandalf.internal.TypeShow
import io.taig.gandalf.syntax.aliases._
import io.taig.gandalf.{ Error, Evaluation, Rule }

class LazyAnd[L <: Rule, R <: Rule.Aux[L#Input]] extends Operator[L, R]

object LazyAnd {
    implicit def evaluation[L <: Rule, R <: Rule.Aux[L#Output]](
        implicit
        lev: Evaluation[L],
        ler: Error[L],
        rev: Evaluation[R],
        rer: Error[R],
        e:   Error[L && R]
    ) = {
        Evaluation.instance[L && R] { input ⇒
            ( lev.validate( input ) andThen rev.validate )
                .leftMap( e.error.map( List( _ ) ).getOrElse( _ ) )
        }
    }

    implicit def show[L <: Rule, R <: Rule.Aux[L#Input]](
        implicit
        l: TypeShow[L],
        r: TypeShow[R]
    ) = TypeShow.instance[L && R]( s"(${l.show} && ${r.show})" )
}