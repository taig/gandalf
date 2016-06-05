package io.taig.gandalf.operator

import io.taig.gandalf._
import io.taig.gandalf.internal.TypeShow
import io.taig.gandalf.syntax.aliases._
import shapeless._
import shapeless.record._
import shapeless.syntax.singleton._

class Apply[L <: Mutation, R <: Validation.In[L#Output]] extends Mutation {
    override type Input = L#Input

    override type Output = R#Output

    override type Arguments = Error.Forward[this.type]
}

object Apply {
    implicit def evaluation[L <: Mutation, R <: Validation.In[L#Output]](
        implicit
        lev: Evaluation[L],
        rev: Evaluation[R],
        e:   Error[L ~> R]
    ) = {
        Evaluation.instance[L ~> R] { input ⇒
            ( lev.validate( input ) andThen rev.validate )
                .leftMap( errors ⇒ e.error( "input" ->> input :: "errors" ->> errors :: HNil ) )
        }
    }

    implicit def show[L <: Mutation, R <: Validation.In[L#Output]](
        implicit
        l: TypeShow[L],
        r: TypeShow[R]
    ) = TypeShow.instance[L ~> R]( s"${l.show} ~> ${r.show}" )

    implicit def error[L <: Mutation, R <: Validation.In[L#Output]]: Error[L ~> R] = new Error[L ~> R] {
        override def error( arguments: Error.Forward[L ~> R] ) = arguments( "errors" )
    }
}