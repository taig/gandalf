package io.taig.bsts.ops

import io.taig.bsts.ops.hlist.Printer
import shapeless.HList

abstract sealed class Evaluation[H <: HList]( name: String )( implicit p: Printer[H] ) {
    def tree: H

    override def toString = s"$name(${p( tree )})"
}

case class Unevaluated[H <: HList]( tree: H )( implicit p: Printer[H] ) extends Evaluation[H]( "Unevaluated" )
case class Computed[H <: HList]( tree: H )( implicit p: Printer[H] ) extends Evaluation[H]( "Computed" )