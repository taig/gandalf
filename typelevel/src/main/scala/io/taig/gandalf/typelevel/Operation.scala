package io.taig.gandalf.typelevel

/**
 * Operations allow to combine Rules
 */
trait Operation[T] extends Rule {
    override final type Input = T
}