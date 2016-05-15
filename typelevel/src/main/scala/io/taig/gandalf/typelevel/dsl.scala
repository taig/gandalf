package io.taig.gandalf.typelevel

import cats.data.Validated._
import cats.data.ValidatedNel
import cats.std.list._
import cats.syntax.cartesian._
import shapeless.Lazy

import scala.language.{ existentials, postfixOps }

trait Operation[T] extends Rule {
    override final type Input = T
}

class And[L <: Rule, R <: Rule.Aux[L#Output]] extends Operation[L#Input]

object And {
    implicit def evaluation[L <: Rule, R <: Rule.Aux[L#Output]](
        implicit
        l: Lazy[Evaluation[L]],
        r: Lazy[Evaluation[R]]
    ) = {
        Evaluation.instance[L And R] { input ⇒
            l.value.validate( input ) andThen r.value.validate
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
        l: Lazy[Evaluation[L]],
        r: Lazy[Evaluation[R]]
    ) = {
        Evaluation.instance[L Apply R] { input ⇒
            l.value.validate( input ) andThen r.value.validate
        }
    }
}

case class Obeys[L, R <: Validation]( value: R#Output ) extends AnyVal {
    override def toString = value.toString
}

trait Evaluation[V <: Validation] {
    def validate( input: V#Input ): ValidatedNel[String, V#Output]
}

object Evaluation {
    def apply[V <: Validation: Evaluation]: Evaluation[V] = Evaluation[V]

    def instance[V <: Validation]( f: V#Input ⇒ ValidatedNel[String, V#Output] ): Evaluation[V] = {
        new Evaluation[V] {
            override def validate( input: V#Input ) = f( input )
        }
    }

    def transformation[T <: Transformation]( f: T#Input ⇒ T#Output ): Evaluation[T] = {
        instance { input ⇒
            valid( f( input ) )
        }
    }
}