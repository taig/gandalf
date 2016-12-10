package io.taig.gandalf.core.syntax

import io.taig.gandalf.core.operation

import scala.language.implicitConversions

trait confirmation {
    implicit def gandalfCoreConfirmation[I]( input: I ): operation.confirmation[I] =
        new operation.confirmation[I]( input )
}

object confirmation extends confirmation