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
        rer: Error[R]
    ) = {
        Evaluation.instance[L And R] { input ⇒
            lev.validate( input ).andThen( input ⇒ rev.validate( input ) )
        }
    }

    implicit def andError[L <: Rule, R <: Rule.Aux[L#Output]](
        implicit
        l: Error[L],
        r: Error[R]
    ) = Error.instance[L And R]( l.error + " " + r.error )
}

class Apply[L <: Mutation, R <: Validation.In[L#Output]] extends Mutation {
    override type Input = L#Input

    override type Output = R#Output
}

object Apply {
    implicit def applyEvaluation[L <: Mutation, R <: Validation.In[L#Output]](
        implicit
        lev: Evaluation[L],
        ler: Error[L],
        rev: Evaluation[R],
        rer: Error[R]
    ) = {
        Evaluation.instance[L Apply R] { input ⇒
            lev.validate( input ).andThen( input ⇒ rev.validate( input ) )
        }
    }

    implicit def applyError[L <: Mutation, R <: Validation.In[L#Output]](
        implicit
        l: Error[L],
        r: Error[R]
    ) = Error.instance[L Apply R]( l.error + " " + r.error )
}