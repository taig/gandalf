package io.taig.gandalf

import cats.data.Validated.{ Invalid, Valid }
import io.circe.{ Decoder, DecodingFailure, Encoder }
import io.taig.gandalf.core.{ Container, Obeys, Validation }

package object circe {
    implicit final def gandalfDecoder[L, C <: Container](
        implicit
        d: Decoder[C#Kind#Input],
        v: Validation[C]
    ): Decoder[L Obeys C] =
        Decoder.instance { x ⇒
            d( x ) match {
                case Right( input ) ⇒
                    v.validate( input ) match {
                        case Valid( output ) ⇒
                            val obeys = Obeys.applyUnsafe[L, C]( output )
                            Right( obeys )
                        case Invalid( errors ) ⇒
                            Left( DecodingFailure( errors.head, x.history ) )
                    }
                case Left( z ) ⇒ Left( z )
            }
        }

    implicit final def gandalfEncoder[L, C <: Container](
        implicit
        e: Encoder[C#Kind#Output]
    ): Encoder[L Obeys C] = Encoder.instance { x ⇒
        e.apply( x.value )
    }
}