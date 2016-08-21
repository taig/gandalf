package io.taig.gandalf.predef

import io.taig.gandalf._
import io.taig.gandalf.predef.iterable.nonEmpty

trait string {
    object required extends ( toIterable ~> nonEmpty )

    sealed class toLower extends Mutation with Symmetric with Input[String]

    object toLower extends toLower {
        implicit val validation = {
            Validation.transformation[toLower.type]( _.toLowerCase )
        }
    }

    sealed class toIterable extends Mutation with Input[String] with Output[Iterable[_]]

    object toIterable extends toIterable {
        implicit val validation = {
            Validation.transformation[toIterable.type]( _.to[Iterable] )
        }
    }

    sealed class toUpper extends Mutation with Symmetric with Input[String]

    object toUpper extends toUpper {
        implicit val validation = {
            Validation.transformation[toUpper.type]( _.toUpperCase )
        }
    }

    sealed class trim extends Mutation with Symmetric with Input[String]

    object trim extends trim {
        implicit val validation = {
            Validation.transformation[trim.type]( _.trim )
        }
    }
}

object string extends string