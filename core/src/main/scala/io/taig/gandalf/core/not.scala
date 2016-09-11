package io.taig.gandalf.core

import cats.data.Validated._

class not[C <: Condition] extends Condition {
    override type Arguments = C#Arguments

    override type Input = C#Input
}

object not {
    implicit def validation[C <: Condition](
        implicit
        v: Validation[C],
        e: Error[not[C]],
        a: Arguments[C]
    ): Validation[not[C]] = Validation.instance[not[C]] { input ⇒
        v.validate( input ) match {
            case Valid( _ ) ⇒
                val arguments = a.collect( input )
                val errors = e.show( arguments )
                invalid( errors )
            case Invalid( _ ) ⇒ valid( input )
        }
    }

    implicit def error[C <: Condition](
        implicit
        e: Error[C]
    ): Error[not[C]] = Error.instance[not[C]] { arguments ⇒
        e.show( arguments ).map( error ⇒ s"not($error)" )
    }

    def apply[C <: Condition]( condition: C ): not[C] = new not[C]
}