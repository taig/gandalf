package io.taig.gandalf.core

import scala.language.implicitConversions

class Obeys[L, C <: Container] private ( val value: C#Kind#Output )
        extends Serializable {
    override def toString = value.toString
}

object Obeys {
    implicit def toValue[L, C <: Container](
        obeys: L Obeys C
    ): C#Kind#Output = obeys.value

    def applyUnsafe[L, C <: Container]( value: C#Kind#Output ): L Obeys C = {
        new Obeys( value )
    }

    def unapply[L, C <: Container](
        obeys: L Obeys C
    ): Option[C#Kind#Output] = Some( obeys.value )
}