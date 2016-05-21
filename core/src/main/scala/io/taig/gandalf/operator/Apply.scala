package io.taig.gandalf.operator

import io.taig.gandalf._
import io.taig.gandalf.internal.TypeShow
import io.taig.gandalf.syntax.aliases._

class Apply[L <: Mutation, R <: Validation.In[L#Output]] extends Mutation {
    override type Input = L#Input

    override type Output = R#Output
}

object Apply {
    implicit def evaluation[L <: Mutation, R <: Validation.In[L#Output]](
        implicit
        lev: Evaluation[L],
        ler: Error[L],
        rev: Evaluation[R],
        rer: Error[R],
        e:   Error[L ~> R]
    ) = {
        Evaluation.instance[L ~> R] { input ⇒
            ( lev.validate( input ) andThen rev.validate )
                .leftMap( e.error.map( List( _ ) ).getOrElse( _ ) )
        }
    }

    implicit def show[L <: Mutation: Evaluation, R <: Validation.In[L#Output]](
        implicit
        l: TypeShow[L],
        r: TypeShow[R]
    ) = TypeShow.instance[L ~> R]( s"${l.show} ~> ${r.show}" )

    implicit val error = Error.none[_ ~> _]
}