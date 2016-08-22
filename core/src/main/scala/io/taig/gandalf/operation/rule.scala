package io.taig.gandalf.operation

import io.taig.gandalf.{ Result, Rule }

final class rule[L <: Rule] {
    //    def &&[R <: Rule.Aux[L#Output]]( right: R ): LazyAnd.Aux[L, R] = {
    //        new LazyAnd {
    //            override final type Left = L
    //
    //            override final type Right = R
    //        }
    //    }
    //
    //    def &[R <: Rule.Aux[L#Output]]( right: R ): EagerAnd.Aux[L, R] = {
    //        new EagerAnd {
    //            override type Left = L
    //
    //            override type Right = R
    //        }
    //    }
}