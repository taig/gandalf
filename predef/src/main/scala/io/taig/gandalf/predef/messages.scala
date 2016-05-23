package io.taig.gandalf.predef

import io.taig.gandalf.operator.{ Apply, Operator }
import io.taig.gandalf.{ Error, Validation }
import shapeless.<:!<

import scala.reflect.{ ClassTag, classTag }

object messages {
    implicit def errorValidation[V <: Validation: ClassTag](
        implicit
        ev1: V <:!< Operator[_, _],
        ev2: V <:!< Apply[_, _]
    ) = Error.instance[V]( s"${classTag[V].runtimeClass.getSimpleName}" )
}