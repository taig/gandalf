package io.taig.bsts.mutation

import io.taig.bsts.mutation.ops.Parser

trait string {
    def parse[T: Parser] = Mutation[String, T]( "parse" )( implicitly[Parser[T]].parse )
}

object string extends string