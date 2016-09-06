package io.taig

import cats.data._

package object gandalf {
    type Result[R <: Rule] = ValidatedNel[String, R#Output]
}