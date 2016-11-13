package io.taig.gandalf

import _root_.doobie.imports._
import io.taig.gandalf.core.{ Container, Obeys }

import scala.reflect.runtime.universe.TypeTag

package object doobie {
    implicit def gandalfDoobieMeta[C <: Container](
        implicit
        m:  Meta[C#Kind#Output],
        tt: TypeTag[C#Kind#Input Obeys C]
    ): Meta[C#Kind#Input Obeys C] = m.xmap( Obeys.applyUnsafe, _.value )
}