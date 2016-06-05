package io.taig.gandalf.syntax

trait all
    extends aliases
    with lift
    with mutation
    with rule
    with transformation
    with validation

object all extends all