package io.taig.gandalf.macros

import io.taig.gandalf.core.Rule

case class Obey[L, R <: Rule]( value: R#Output )
    extends AnyVal
    with Serializable