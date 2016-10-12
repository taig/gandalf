package io.taig.gandalf.core

import shapeless.{ HList, Witness }

import scala.reflect._

trait Rule {
    type Input

    type Output

    type Arguments <: HList
}

object Rule {
    type Input[I] = Rule { type Input = I }

    type Output[O] = Rule { type Output = O }

    type Aux[I, O] = Rule { type Input = I; type Output = O }

    trait Applyable extends Rule with Container.Id {
        def apply( input: Input )(
            implicit
            e: Error[this.type]
        ): Result[this.type]

        def arguments( input: Input ): Arguments
    }

    object Applyable {
        implicit def validation[A <: Applyable](
            implicit
            w: Witness.Aux[A],
            e: Error[A]
        ): Validation[A] = Validation.instance[A] { input ⇒
            w.value.apply(
                input.asInstanceOf[w.value.Input]
            )( e.asInstanceOf[Error[w.value.type]] )
        }

        implicit def arguments[A <: Applyable](
            implicit
            w: Witness.Aux[A]
        ): Arguments[A] = Arguments.instance[A] { input ⇒
            w.value.arguments( input.asInstanceOf[w.value.Input] )
        }

        implicit def error[A <: Applyable: ClassTag]: Error[A] = {
            Error.static( name[A] )
        }

        implicit def errorNot[A <: Applyable](
            implicit
            e: Error[A]
        ): Error[not[A]] = Error.instance[not[A]] { arguments ⇒
            e.show( arguments ).map( error ⇒ s"not($error)" )
        }

        implicit def serialization[A <: Applyable: ClassTag]: Serialization[A] = {
            Serialization.instance( name[A] )
        }

        private def name[A: ClassTag]: String = {
            classTag[A].runtimeClass.getSimpleName.replace( "$", "" )
        }

        def implicits[A <: Applyable]( f: ⇒ A )(
            implicit
            e: Error[A]
        ): Validation[A] with Arguments[A] = {
            val applyable = f

            new Validation[A] with Arguments[A] {
                override def validate( input: A#Input ) = {
                    applyable.apply( input.asInstanceOf[applyable.Input] )
                }

                override def collect( input: A#Input ) = {
                    applyable.arguments( input.asInstanceOf[applyable.Input] )
                }
            }
        }
    }
}