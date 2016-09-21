package io.taig.gandalf

import cats.data._

package object core {
    type Result[R <: Rule] = ValidatedNel[String, R#Output]
}