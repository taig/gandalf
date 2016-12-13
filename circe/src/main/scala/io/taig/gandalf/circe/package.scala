package io.taig.gandalf

import cats.data.NonEmptyList
import cats.syntax.either._
import io.circe.CursorOp.DownField
import io.circe.Decoder.Result
import io.circe.parser._
import io.circe.syntax._
import io.circe.{ Decoder, DecodingFailure, Encoder, Error, Json, ParsingFailure }
import io.taig.gandalf.report._

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

    implicit final def gandalfCirceDecoderObeys[R <: Rule, I, O](
        implicit
        d: Decoder[I],
        v: Validation[R, I, O],
        r: Report[R, I, O]
    ): Decoder[Obeys[R, I, O]] = Decoder.instance { cursor ⇒
        d( cursor ) match {
            case Right( input ) ⇒
                v.confirm( input ) match {
                    case Some( output ) ⇒
                        Right( Obeys.applyUnsafe[R, I, O]( output ) )
                    case None ⇒
                        val errors = r.show( input )
                        val failure = DecodingFailure(
                            errors.asJson.noSpaces,
                            cursor.history
                        )
                        Left( failure )
                }
            case l @ Left( _ ) ⇒ l.asInstanceOf[Result[Obeys[R, I, O]]]
        }
    }

    implicit final def gandalfCirceEncoderObeys[R <: Rule, I, O](
        implicit
        e: Encoder[O]
    ): Encoder[Obeys[R, I, O]] = Encoder.instance( e( _ ) )
}