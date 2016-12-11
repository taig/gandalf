package io.taig.gandalf.syntax

import io.taig.gandalf._

import scala.language.implicitConversions

trait confirmation {
    implicit def gandalfCoreConfirmation[I]( input: I ): operation.confirmation[I] =
        new operation.confirmation[I]( input )
}

object confirmation extends confirmation