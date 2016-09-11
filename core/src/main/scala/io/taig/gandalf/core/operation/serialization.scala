package io.taig.gandalf.core.operation

import io.taig.gandalf.core.{ Rule, Serialization }

final class serialization[R <: Rule]( rule: R ) {
    def serialize( implicit s: Serialization[R] ): String = s.serialize
}