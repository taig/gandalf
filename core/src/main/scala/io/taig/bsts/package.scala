package io.taig

import shapeless._

import scala.language.implicitConversions

package object bsts {
    implicit def `Rule -> Policy`[I <: String, T, A <: HList]( rule: Rule[I, T, A] ): Policy[T, Rule[I, T, A] :: HNil] = {
        Policy( rule )
    }
}