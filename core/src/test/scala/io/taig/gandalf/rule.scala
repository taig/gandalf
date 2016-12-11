package io.taig.gandalf

import scala.util.Try

object condition {
    trait success extends Rule.Condition

    object success extends success {
        implicit val validation: Validation[success, String, String] = {
            Validation.condition( _ ⇒ true )
        }

        implicit def option[T]: Validation[success, Option[T], Option[T]] = {
            Validation.condition( _ ⇒ true )
        }
    }

    trait failure extends Rule.Condition

    object failure extends failure {
        implicit val validation: Validation[failure, String, String] = {
            Validation.condition( _ ⇒ false )
        }

        implicit def option[T]: Validation[failure, Option[T], Option[T]] = {
            Validation.condition( _ ⇒ false )
        }
    }

    trait int extends Rule.Condition

    object int extends int {
        implicit val validation: Validation[int, Int, Int] = {
            Validation.condition( _ ⇒ true )
        }
    }
}

object mutation {
    trait success extends Rule.Mutation

    object success extends success {
        implicit val validation: Validation[success, String, String] = {
            Validation.mutation( Some( _ ) )
        }
    }

    trait failure extends Rule.Mutation

    object failure extends failure {
        implicit val validation: Validation[failure, String, String] = {
            Validation.mutation( _ ⇒ None )
        }
    }

    trait int extends Rule.Mutation

    object int extends int {
        implicit val validation: Validation[int, Int, Int] = {
            Validation.mutation( Some( _ ) )
        }
    }

    trait intString extends Rule.Mutation

    object intString extends intString {
        implicit val validation: Validation[intString, Int, String] = {
            Validation.mutation( int ⇒ Some( int.toString ) )
        }
    }

    trait stringInt extends Rule.Mutation

    object stringInt extends stringInt {
        implicit val validation: Validation[stringInt, String, Int] = {
            Validation.mutation( string ⇒ Try( string.toInt ).toOption )
        }
    }
}

object transition {
    trait string extends Rule.Transition

    object string extends string {
        implicit val validation: Validation[string, String, String] = {
            Validation.transition( identity )
        }
    }

    trait int extends Rule.Transition

    object int extends int {
        implicit val validation: Validation[int, Int, Int] = {
            Validation.transition( identity )
        }
    }

    trait intString extends Rule.Transition

    object intString extends intString {
        implicit val validation: Validation[intString, Int, String] = {
            Validation.transition( _.toString )
        }
    }

    trait stringInt extends Rule.Transition

    object stringInt extends stringInt {
        implicit val validation: Validation[stringInt, String, Int] = {
            Validation.transition( _.toInt )
        }
    }
}

object composition {
    object and {
        class success extends ( condition.success && condition.success )
        object success extends success

        class failure extends ( condition.success && condition.failure )
        object failure extends failure

        class notL extends ( not[condition.failure] && condition.success )
        object notL extends notL

        class notR extends ( condition.success && not[condition.failure] )
        object notR extends notR
    }

    object or {
        class success extends ( condition.failure || condition.success )
        object success extends success

        class failure extends ( condition.failure || condition.failure )
        object failure extends failure

        class notL extends ( not[condition.failure] || condition.failure )
        object notL extends notL

        class notR extends ( condition.failure || not[condition.failure] )
        object notR extends notR
    }
}