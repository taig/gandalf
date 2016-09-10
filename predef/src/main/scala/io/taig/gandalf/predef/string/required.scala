package io.taig.gandalf.predef.string

import io.taig.gandalf.core.{ not, ~> }

object required extends ( trim.type ~> not[isEmpty.type] )