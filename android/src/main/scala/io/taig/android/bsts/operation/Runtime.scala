package io.taig.android.bsts.operation

import io.taig.bsts._
import io.taig.bsts
import io.taig.bsts.rule.Required

case class Runtime[R <: Rule]( rule: R )(
        implicit
        definition: Definition[R],
        show:       Show[R],
        adjust:     Adjust[R],
        empty:      Empty[R]
) extends bsts.Validatable[R#Value] {
    override def validate( value: R#Value ) = {
        val adjusted = adjust.adjust( value )

        !rule.isInstanceOf[Required[_]] && empty.isEmpty( adjusted ) match {
            case true  ⇒ Success( adjusted )
            case false ⇒ new ValidatableRule( rule )( definition, show, Adjust( identity ) ).validate( adjusted )
        }
    }
}