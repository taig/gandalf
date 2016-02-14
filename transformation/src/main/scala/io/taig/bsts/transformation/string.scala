package io.taig.bsts.transformation

trait string {
    val trim = Transformation[String, String]( "trim" )( _.trim )
}

object string extends string