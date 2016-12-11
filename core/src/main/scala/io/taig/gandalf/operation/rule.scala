package io.taig.gandalf.operation

import io.taig.gandalf._

final class rule[R <: Rule]( val rule: R ) extends AnyVal {
    def serialize( implicit s: Serialization[R] ): String = s.serialize
}