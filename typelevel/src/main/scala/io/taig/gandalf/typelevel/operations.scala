package io.taig.gandalf.typelevel

/**
 * Operations allow to combine Rules
 */
trait Operation[T] extends Rule {
    override final type Input = T
}

class And[L <: Rule, R <: Rule.Aux[L#Output]] extends Operation[L#Input]

object And {
    implicit def evaluation[L <: Rule, R <: Rule.Aux[L#Output]](
        implicit
        lev: Evaluation[L],
        ler: Error[L],
        rev: Evaluation[R],
        rer: Error[R],
        e:   Error[L And R]
    ) = {
        Evaluation.instance[L And R] { input ⇒
            ( lev.validate( input ) andThen rev.validate )
                .leftMap( e.error.map( List( _ ) ).getOrElse( _ ) )
        }
    }

    implicit val error = Error.none[_ And _]
}

class Apply[L <: Mutation, R <: Validation.In[L#Output]] extends Mutation {
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