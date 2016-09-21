package io.taig.gandalf.core.test

import io.taig.gandalf.core.Transformation

object transformation extends Transformation.With[String, String]( identity )