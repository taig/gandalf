package io.taig.bsts.mutation.ops

import scala.language.{ reflectiveCalls, higherKinds }
import scala.util.Try

trait Extraction[T, F[_]] {
    def extract( value: F[T] ): Option[T]
}

object Extraction {
    def instance[T, F[_]]( f: F[T] ⇒ Option[T] ): Extraction[T, F] = new Extraction[T, F] {
        override def extract( value: F[T] ): Option[T] = f( value )
    }

    implicit def `Extract[Either]`[L, R]: Extraction[R, ( { type λ[β] = Either[L, β] } )#λ] = {
        instance[R, ( { type λ[β] = Either[L, β] } )#λ]( _.right.toOption )
    }

    implicit def `Extract[Option]`[T]: Extraction[T, Option] = instance( identity )

    implicit def `Extract[Try]`[T]: Extraction[T, Try] = instance( _.toOption )
}