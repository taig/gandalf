package io.taig.gandalf

package object predef {
//    @rule( matches( "^[^@]+@[^@]+\\.[^@]+$" ) )
    class email extends matches["^[^@]+@[^@]+\\.[^@]+$"]
    object email extends email

    class gte[T: ValueOf] extends ( equal[T] || gt[T] )

    object gte {
        def apply[T]( value: T ): gte[value.type] = new gte[value.type]
    }

    class lte[T: ValueOf] extends ( equal[T] || lt[T] )

    object lte {
        def apply[T]( value: T ): lte[value.type] = new lte[value.type]
    }

    class negative extends lt[0]
    object negative extends negative

    class positive extends gt[0]
    object positive extends positive

    class required extends ( trim && not[empty] )
    object required extends required

//    @rule( matches( "^(https?:\\/\\/)?.+\\..+" ) )
    class url extends matches["^(https?:\\/\\/)?.+\\..+"]
    object url extends url

    class zero extends equal[0]
    object zero extends zero
}