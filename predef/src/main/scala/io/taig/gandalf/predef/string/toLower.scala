package io.taig.gandalf.predef.string

import io.taig.gandalf.core.Transformation

object toLower extends Transformation.With[String, String]( _.toLowerCase )