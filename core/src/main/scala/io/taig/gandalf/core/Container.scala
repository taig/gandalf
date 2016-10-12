package io.taig.gandalf.core

trait Container {
    type Kind <: Rule
}

object Container {
    trait Id extends Container { this: Rule ⇒
        override final type Kind = this.type
    }
}