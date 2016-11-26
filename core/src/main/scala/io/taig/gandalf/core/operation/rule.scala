package io.taig.gandalf.core.operation

import io.taig.gandalf.core.{ Rule, Serialization }

final class rule[R <: Rule]( val rule: R ) extends AnyVal {
    def serialize( implicit s: Serialization[R] ): String = s.serialize
}