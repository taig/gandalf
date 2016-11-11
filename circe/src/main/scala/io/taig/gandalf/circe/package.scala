package io.taig.gandalf

import cats.data.Validated.{ Invalid, Valid }
import io.circe.Decoder.Result
import io.circe.{ Decoder, DecodingFailure, Encoder }
import io.taig.gandalf.core.{ Container, Obeys, Validation }

package object circe {
    implicit final def gandalfCirceDecoder[L, C <: Container](
        implicit
        d: Decoder[C#Kind#Input],
        v: Validation[C]
    ): Decoder[L Obeys C] = Decoder.instance { cursor ⇒
        d( cursor ) match {
            case Right( input ) ⇒ v.validate( input ) match {
                case Valid( output ) ⇒
                    val obeys = Obeys.applyUnsafe[L, C]( output )
                    Right( obeys )
                case Invalid( errors ) ⇒
                    Left( DecodingFailure( errors.head, cursor.history ) )
            }
            case l @ Left( _ ) ⇒ l.asInstanceOf[Result[L Obeys C]]
        }
    }

    implicit final def gandalfCirceEncoder[L, C <: Container](
        implicit
        e: Encoder[C#Kind#Output]
    ): Encoder[L Obeys C] = Encoder.instance {
        case Obeys( value ) ⇒ e.apply( value )
    }
}