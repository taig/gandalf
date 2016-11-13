package io.taig.gandalf

import cats.data.NonEmptyList
import cats.syntax.either._
import cats.data.Validated.{ Invalid, Valid }
import io.circe.CursorOp.DownField
import io.circe.Decoder.Result
import io.circe.{ Decoder, DecodingFailure, Encoder, Error, Json, ParsingFailure }
import io.circe.parser._
import io.circe.syntax._
import io.taig.gandalf.core.{ Container, Obeys, Validation }

package object circe {
    implicit val gandalfCirceEncoderDecodingFailure: Encoder[DecodingFailure] = {
        Encoder.instance { error ⇒
            val message = parse( error.message )
                .getOrElse( List( error.message ).asJson )

            error.history
                .flatMap( _.op )
                .collect { case DownField( field ) ⇒ field }
                .foldLeft( message ) {
                    case ( json, field ) ⇒ Json.obj( field → json )
                }
        }
    }

    implicit val gandalfCirceEncoderParsingFailure: Encoder[ParsingFailure] = {
        Encoder[String].contramap( _.message )
    }

    implicit val gandalfCirceEncoderNonEmptyListError: Encoder[NonEmptyList[Error]] = {
        val empty = Json.obj()
        Encoder.instance( _.foldLeft( empty ) { _ deepMerge _.asJson } )
    }

    implicit val gandalfCirceEncoderError: Encoder[Error] = {
        Encoder.instance {
            case decoding: DecodingFailure ⇒
                gandalfCirceEncoderDecodingFailure( decoding )
            case parsing: ParsingFailure ⇒
                gandalfCirceEncoderParsingFailure( parsing )
        }
    }

    implicit final def gandalfCirceDecoderObeys[L, C <: Container](
        implicit
        d: Decoder[C#Kind#Input],
        v: Validation[C]
    ): Decoder[L Obeys C] = Decoder.instance { cursor ⇒
        d( cursor ) match {
            case Right( input ) ⇒ v.validate( input ) match {
                case Valid( output ) ⇒
                    Right( Obeys.applyUnsafe[L, C]( output ) )
                case Invalid( errors ) ⇒
                    val failure = DecodingFailure(
                        errors.asJson.noSpaces,
                        cursor.history
                    )
                    Left( failure )
            }
            case l @ Left( _ ) ⇒ l.asInstanceOf[Result[L Obeys C]]
        }
    }

    implicit final def gandalfCirceEncoderObeys[L, C <: Container](
        implicit
        e: Encoder[C#Kind#Output]
    ): Encoder[L Obeys C] = Encoder.instance {
        case Obeys( value ) ⇒ e.apply( value )
    }
}