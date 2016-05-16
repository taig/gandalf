package io.taig.gandalf.typelevel.ops

import io.taig.gandalf.typelevel.{ Evaluation, Rule }
import io.taig.gandalf.typelevel

class RuleOps[L <: Rule]( left: Evaluation[L] ) {
    def &&[R <: Rule.Aux[L#Output]]( right: Evaluation[R] ) = typelevel.&&( left, right )
}