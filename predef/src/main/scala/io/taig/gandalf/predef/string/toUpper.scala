package io.taig.gandalf.predef.string

import io.taig.gandalf.core.Transformation

object toUpper extends Transformation.With[String, String]( _.toUpperCase )