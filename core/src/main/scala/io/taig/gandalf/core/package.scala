package io.taig.gandalf

import cats.data._

package object core {
    type Result[C <: Container] = ValidatedNel[String, C#Kind#Output]
}