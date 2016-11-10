package io.taig.gandalf.core

class Obeys[L, C <: Container] private ( val value: C#Kind#Output )
        extends AnyVal
        with Serializable {
    override def toString = value.toString
}

object Obeys {
    private[gandalf] def applyUnsafe[L, C <: Container](
        value: C#Kind#Output
    ): L Obeys C = new Obeys( value )
}