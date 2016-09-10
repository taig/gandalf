package io.taig.gandalf.predef.string

import io.taig.gandalf.core.Transformation

object ltrim extends Transformation.With[String, String](
    _.replaceFirst( "^\\s*", "" )
)