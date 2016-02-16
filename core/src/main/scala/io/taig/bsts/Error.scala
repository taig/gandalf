package io.taig.bsts

import shapeless._
import shapeless.ops.hlist.ToTraversable

case class Error[+N <: String, +A <: HList]( arguments: A )(
        implicit
        w:  Witness.Aux[N],
        tt: ToTraversable.Aux[A, List, Any]
) {
    def name: String = w.value

    override def toString = {
        val arguments = this.arguments.toList match {
            case Nil       ⇒ ""
            case arguments ⇒ s", (${arguments.mkString( ", " )})"
        }

        s"Error($name$arguments)"
    }
}