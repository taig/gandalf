package io.taig.gandalf.core.syntax

import io.taig.gandalf.core._

import scala.language.implicitConversions

trait validation {
    implicit def gandalfCoreGeneric[I]( input: I ): operation.generic[I] = {
        new operation.generic[I]( input )
    }

    implicit def gandalfCoreOperation[R, I, O](
        rule: R
    ): operation.operation[R, I, O] = new operation.operation[R, I, O]( rule )

    implicit def gandalfCoreRule[R](
        rule: R
    ): operation.rule[R] = new operation.rule[R]( rule )
}

object validation extends validation