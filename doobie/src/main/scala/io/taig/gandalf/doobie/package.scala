package io.taig.gandalf

import _root_.doobie.imports._

import scala.reflect.runtime.universe.TypeTag

package object doobie {
    implicit def gandalfDoobieMeta[R <: Rule, I, O](
        implicit
        m:  Meta[O],
        tt: TypeTag[Obeys[R, I, O]]
    ): Meta[Obeys[R, I, O]] = m.xmap( Obeys.applyUnsafe, _.value )
}