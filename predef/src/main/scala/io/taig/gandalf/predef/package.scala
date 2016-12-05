package io.taig.gandalf

import io.taig.gandalf.core.{ &&, not }

package object predef {
    object email extends matches["^[^@]+@[^@]+\\.[^@]+$"]

    object required extends ( trim && not[empty] )

    object url extends matches["^(https?:\\/\\/)?.+\\..+"]
}