package io.taig.bsts.transformations

import io.taig.bsts.Transformation

trait string {
    val trim = Transformation[String, String]( "trim" ) from ( _.trim )
}

object string extends string