package io.taig.bsts.transformation

trait string {
    val trim = Transformation[String, String]( "trim" )( _.trim )

    val noSpaces = Transformation[String, String]( "noSpaces" )( _.replaceAll( "\\s", "" ) )
}

object string extends string