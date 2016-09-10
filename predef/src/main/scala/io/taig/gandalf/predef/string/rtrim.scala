package io.taig.gandalf.predef.string

import io.taig.gandalf.core.Transformation

object rtrim extends Transformation.With[String, String](
    _.replaceFirst( "\\s*$", "" )
)