package io.taig.bsts

import io.taig.bsts.ops.hlist.Printer
import shapeless.HList

abstract class Evaluation[H <: HList]( implicit p: Printer[H] ) {
    def tree: H

    override def toString = s"${getClass.getSimpleName}(${p( tree )})"
}

case class Unevaluated[H <: HList]( tree: H )( implicit p: Printer[H] ) extends Evaluation[H]
case class Computed[H <: HList]( tree: H )( implicit p: Printer[H] ) extends Evaluation[H]