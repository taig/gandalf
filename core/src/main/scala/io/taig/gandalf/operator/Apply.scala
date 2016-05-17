package io.taig.gandalf.operator

import io.taig.gandalf.{ Error, Evaluation, Mutation, Validation }

case class Apply[L <: Mutation, R <: Validation.In[L#Output]](
        left:  Evaluation[L],
        right: Evaluation[R]
) extends Mutation {
    override type Input = L#Input

    override type Output = R#Output
}

object Apply {
    implicit def evaluation[L <: Mutation: Evaluation, R <: Validation.In[L#Output]](
        implicit
        lev: Evaluation[L],
        ler: Error[L],
        rev: Evaluation[R],
        rer: Error[R],
        e:   Error[L Apply R]
    ) = {
        Evaluation.instance[L Apply R] { input ⇒
            ( lev.validate( input ) andThen rev.validate )
                .leftMap( e.error.map( List( _ ) ).getOrElse( _ ) )
        }
    }

    implicit val error = Error.none[_ Apply _]
}