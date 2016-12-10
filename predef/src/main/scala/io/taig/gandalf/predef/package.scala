package io.taig.gandalf

import io.taig.gandalf.core.{&&, not, ||}

package object predef {
    object email extends matches["^[^@]+@[^@]+\\.[^@]+$"]

    class gte[T: ValueOf] extends ( equal[T] || gt[T] )

    object gte {
        def apply[T]( value: T ): gte[value.type] = new gte[value.type]
    }

    class lte[T: ValueOf] extends ( equal[T] || lt[T] )

    object lte {
        def apply[T]( value: T ): lte[value.type] = new lte[value.type]
    }

    object negative extends lt[0]

    object positive extends gt[0]

    object required extends ( trim && not[empty] )

    object url extends matches["^(https?:\\/\\/)?.+\\..+"]
    
    object zero extends equal[0]
}