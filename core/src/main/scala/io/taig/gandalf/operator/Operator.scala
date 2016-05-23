package io.taig.gandalf.operator

import io.taig.gandalf.{ Error, Rule }
import shapeless.record._

trait Operator[L <: Rule, R <: Rule.Aux[L#Output]] extends Rule {
    override final type Input = L#Input

    override final type Arguments = Error.Forward[this.type]
}

object Operator {
    implicit def error[L <: Rule, R <: Rule.Aux[L#Input]]: Error[L Operator R] = new Error[L Operator R] {
        override def error( arguments: Error.Forward[L Operator R] ) = arguments( "errors" )
    }
}