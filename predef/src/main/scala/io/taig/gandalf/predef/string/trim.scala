package io.taig.gandalf.predef.string

import io.taig.gandalf.core.Transformation

object trim extends Transformation.With[String, String]( _.trim )