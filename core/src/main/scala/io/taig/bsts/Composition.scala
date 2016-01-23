package io.taig.bsts

import shapeless.HList

/**
 * Rule composition strategies
 */
sealed trait Composition[I <: String, T, A <: HList] {
    def rule: Rule[I, T, A]
}

object Composition {
    case class And[I <: String, T, A <: HList]( rule: Rule[I, T, A] ) extends Composition[I, T, A]
    case class KeepAnd[I <: String, T, A <: HList]( rule: Rule[I, T, A] ) extends Composition[I, T, A]
    case class AndKeep[I <: String, T, A <: HList]( rule: Rule[I, T, A] ) extends Composition[I, T, A]
    case class Or[I <: String, T, A <: HList]( rule: Rule[I, T, A] ) extends Composition[I, T, A]
}