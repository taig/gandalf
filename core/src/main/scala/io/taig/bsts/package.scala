package io.taig

package object bsts {
    implicit class ValidatableRule[R <: Rule]( rule: R )(
        implicit
        definition: Definition[R],
        show:       Show[R],
        adjust:     Adjust[R]
    ) extends operation.rule.Validatable[R]( rule )
}