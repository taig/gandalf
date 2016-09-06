package io.taig.gandalf

import cats.data.Validated._

final class not[C <: Condition] extends Condition {
    override type Input = C#Input
}

object not {
    implicit def validation[C <: Condition](
        implicit
        v: Validation[C],
        e: Error[C],
        a: Arguments[C]
    ): Validation[not[C]] = new Validation[not[C]] {
        override def validate( input: C#Input ) = {
            v.validate( input ) match {
                case Valid( _ ) ⇒
                    val arguments = a.collect( input )
                    val errors = e.show( arguments )
                    invalid( errors )
                case Invalid( _ ) ⇒ valid( input )
            }
        }
    }

    def apply[C <: Condition]( condition: C ): not[C] = new not[C]
}