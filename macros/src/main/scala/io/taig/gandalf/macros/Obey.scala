package io.taig.gandalf.macros

import io.taig.gandalf.core.Container

case class Obey[L, C <: Container]( value: C#Kind#Output )
    extends AnyVal
    with Serializable