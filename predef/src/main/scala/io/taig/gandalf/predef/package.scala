package io.taig.gandalf

import io.taig.gandalf.macros._
import io.taig.gandalf.implicits._

package object predef {
    @rule( matches( "^[^@]+@[^@]+\\.[^@]+$" ) )
    class email

    class gte[T: ValueOf] extends ( equal[T] || gt[T] )

    object gte {
        def apply[T]( value: T ): gte[value.type] = new gte[value.type]
    }

    class lte[T: ValueOf] extends ( equal[T] || lt[T] )

    object lte {
        def apply[T]( value: T ): lte[value.type] = new lte[value.type]
    }

    @rule( lt( 0 ) )
    class negative

    @rule( gt( 0 ) )
    class positive

    @rule( trim && not( empty ) )
    class required

    @rule( matches( "^(https?:\\/\\/)?.+\\..+" ) )
    class url

    @rule( equal( 0 ) )
    class zero
}