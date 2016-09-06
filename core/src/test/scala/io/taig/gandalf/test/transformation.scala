package io.taig.gandalf.test

import io.taig.gandalf.Transformation

object transformation extends Transformation.With[String, String]( identity )
