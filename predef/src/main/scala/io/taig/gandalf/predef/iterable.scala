package io.taig.gandalf.predef

import io.taig.gandalf.{ Arguments, Input, Rule, Validation }

trait iterable {
    class isEmpty extends Rule with Input[Iterable[_]] with Arguments.Input

    object isEmpty extends isEmpty {
        implicit val validation = {
            Validation.rule[isEmpty.type]( _.isEmpty )( identity )
        }
    }

    class nonEmpty extends Rule with Input[Iterable[_]] with Arguments.Input

    object nonEmpty extends nonEmpty {
        implicit val validation = {
            Validation.rule[nonEmpty.type]( _.nonEmpty )( identity )
        }
    }
}

object iterable extends iterable