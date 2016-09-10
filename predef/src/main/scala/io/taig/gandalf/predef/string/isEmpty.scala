package io.taig.gandalf.predef.string

import io.taig.gandalf.core.{ Condition, Reportable }

object isEmpty
    extends Condition.With[String]( _.isEmpty )
    with Reportable.Input