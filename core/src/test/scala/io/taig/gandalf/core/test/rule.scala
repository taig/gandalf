package io.taig.gandalf.core.test

import io.taig.gandalf.core.Validation._

object condition {
    trait success

    object success extends success {
        implicit def generic[T]: Condition[success, T] = {
            Condition.instance( _ ⇒ true )
        }
    }

    trait failure

    object failure extends failure {
        implicit def generic[T]: Condition[failure, T] = {
            Condition.instance( _ ⇒ false )
        }
    }
}

object mutation {
    trait success

    object success extends success {
        implicit def generic[T]: Mutation[success, T, T] = {
            Mutation.instance( Some( _ ) )
        }
    }

    trait failure

    object failure extends failure {
        implicit def generic[T]: Mutation[failure, T, T] = {
            Mutation.instance( _ ⇒ None )
        }
    }
}

trait transformation

object transformation extends transformation {
    implicit def generic[T]: Transformation[transformation, T, T] = {
        Transformation.instance( identity )
    }
}