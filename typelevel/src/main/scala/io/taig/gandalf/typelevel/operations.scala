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
        l: Evaluation[L],
        r: Evaluation[R]
    ) = {
        Evaluation.instance[L And R] { input ⇒
            l.validate( input ) andThen r.validate
        }
    }
}

class Apply[L <: Mutation, R <: Validation.In[L#Output]] extends Mutation {
    override type Input = L#Input

    override type Output = R#Output
}

object Apply {
    implicit def evaluation[L <: Mutation, R <: Validation.In[L#Output]](
        implicit
        l: Evaluation[L],
        r: Evaluation[R]
    ) = {
        Evaluation.instance[L Apply R] { input ⇒
            l.validate( input ) andThen r.validate
        }
    }
}