package io.taig.gandalf.core.operation

import io.taig.gandalf.core._

final class rule[R]( val rule: R ) extends AnyVal {
    def serialize( implicit s: Serialization[R] ): String = s.serialize
}