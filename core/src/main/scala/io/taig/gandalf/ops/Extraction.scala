package io.taig.gandalf.ops

import cats.data.Xor

import scala.language.{ higherKinds, reflectiveCalls }
import scala.util.Try

trait Extraction[T, F[_]] {
    def extract( value: F[T] ): Option[T]
}

object Extraction {
    def instance[T, F[_]]( f: F[T] ⇒ Option[T] ): Extraction[T, F] = new Extraction[T, F] {
        override def extract( value: F[T] ): Option[T] = f( value )
    }

    implicit def extractionEither[L, R]: Extraction[R, ( { type λ[β] = Either[L, β] } )#λ] = {
        instance[R, ( { type λ[β] = Either[L, β] } )#λ]( _.right.toOption )
    }

    implicit def extractionOption[T]: Extraction[T, Option] = instance( identity )

    implicit def extractionTry[T]: Extraction[T, Try] = instance( _.toOption )

    implicit def extractionXor[L, R]: Extraction[R, ( { type λ[β] = Xor[L, β] } )#λ] = {
        instance[R, ( { type λ[β] = Xor[L, β] } )#λ]( _.toOption )
    }
}