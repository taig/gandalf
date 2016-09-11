package io.taig.gandalf.predef.string

import io.taig.gandalf.core.{ Arguments, Condition }

object isEmpty
    extends Condition.With[String]( _.isEmpty )
    with Arguments.Input