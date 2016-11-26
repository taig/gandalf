package io.taig.gandalf

import io.taig.gandalf.core._
import io.taig.gandalf.core.goaway.not

package object predef {
    trait trim

    object trim extends trim {
        implicit val transformationString: Transformation[trim, String, String] = {
            Transformation.instance( _.trim )
        }
    }

    trait required extends ( trim && not[empty] )

    object required extends required {
        implicit val ruleString: Condition[required, String] = {
            Condition.instance( _.length > 0 )
        }
    }

    trait empty

    object empty extends empty {
        implicit val ruleString: Condition[empty, String] = {
            Condition.instance( _.length == 0 )
        }

        implicit val ruleList: Condition[empty, List[_]] = {
            Condition.instance( _.isEmpty )
        }
    }

    class matches[T]

    object matches {
        def apply[T]( value: T ): matches[value.type] = new matches[value.type]

        implicit def ruleGeneric[T: ValueOf, S >: T]: Condition[matches[T], S] = {
            Condition.instance( _ == valueOf[T] )
        }
    }
    
    object length {
        class min[T <: Int]

        object min {
            def apply( value: Int ): min[value.type] = new min[value.type]

            implicit def ruleString[T <: Int: ValueOf]: Condition[min[T], String] = {
                Condition.instance( _.length >= valueOf[T] )
            }
        }
    }
}