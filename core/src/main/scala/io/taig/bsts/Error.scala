package io.taig.bsts

import shapeless._

case class Error[N <: String, A <: HList]( arguments: A )(
        implicit
        w: Witness.Aux[N]
) {
    def name: String = w.value

    override def toString = {
        val arguments = this.arguments.runtimeList match {
            case Nil       ⇒ ""
            case arguments ⇒ s", (${arguments.mkString( ", " )})"
        }

        s"Error($name$arguments)"
    }
}