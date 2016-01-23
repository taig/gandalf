package io.taig.bsts

import io.taig.bsts
import shapeless._
import shapeless.ops.hlist._

case class Policy[T, R <: HList]( rules: R ) {
    def combine[I <: String, A <: HList, O <: HList]( rule: Rule[I, T, A] )(
        implicit
        p: Prepend.Aux[R, Rule[I, T, A] :: HNil, O]
    ): Policy[T, O] = Policy( rules :+ rule )

    def validate[O1 <: HList, O2 <: HList, O3 <: HList, O4 <: HList]( value: T )(
        implicit
        m:   Mapper.Aux[Policy.validate.type, R, O1],
        cm:  ConstMapper.Aux[T, R, O2],
        za:  ZipApply.Aux[O1, O2, O3],
        lf1: Policy.Validation.isSuccess.F[O3],
        lf2: Policy.Validation.raw.F[O3]
    ): Policy.Validation[O3] = {
        val functions = rules map Policy.validate
        val values = rules mapConst value
        Policy.Validation( functions zipApply values )
    }
}

object Policy {
    def apply[I <: String, T, A <: HList]( rule: Rule[I, T, A] ): Policy[T, Rule[I, T, A] :: HNil] = {
        Policy( rule :: HNil )
    }

    object validate extends Poly1 {
        implicit def default[I <: String, T, A <: HList] = at[Rule[I, T, A]]( rule ⇒ rule( _: T ) )
    }

    case class Validation[V <: HList]( result: V )(
            implicit
            lf1: Validation.isSuccess.F[V],
            lf2: Validation.raw.F[V]
    ) extends bsts.Validation[Validation.report.F[V], List[String], List[( String, List[Any] )]] {
        override def isSuccess: Boolean = {
            result.foldLeft( true )( Validation.isSuccess )
        }

        override def report( implicit lf: Validation.report.F[V] ): List[String] = {
            result.foldLeft( List.empty[String] )( Validation.report )
        }

        override def raw: List[( String, List[Any] )] = {
            result.foldLeft( List.empty[( String, List[Any] )] )( Validation.raw )
        }

        override def toString = raw match {
            case Nil ⇒ "Success"
            case errors ⇒
                val list = errors.map {
                    case ( identifier, Nil )    ⇒ s"$identifier"
                    case ( identifier, errors ) ⇒ s"($identifier, (${errors.mkString( ", " )}))"
                }

                s"Failures(${list.mkString( ", " )})"
        }
    }

    object Validation {
        object isSuccess extends Poly2 {
            type F[V <: HList] = LeftFolder.Aux[V, Boolean, isSuccess.type, Boolean]

            implicit def default[I <: String, T, A <: HList] = {
                at[Boolean, Either[Error[I, A], T]] {
                    case ( isSuccess, Right( _ ) ) ⇒ isSuccess
                    case ( isSuccess, Left( _ ) )  ⇒ false
                }
            }
        }

        object report extends Poly2 {
            type F[V <: HList] = LeftFolder.Aux[V, List[String], report.type, List[String]]

            implicit def default[I <: String, T, A <: HList]( implicit r: Report[I, A] ) = {
                at[List[String], Either[Error[I, A], T]] {
                    case ( reports, Right( _ ) )    ⇒ reports
                    case ( reports, Left( error ) ) ⇒ reports :+ error.report
                }
            }
        }

        object raw extends Poly2 {
            type F[V <: HList] = LeftFolder.Aux[V, List[( String, List[Any] )], raw.type, List[( String, List[Any] )]]

            implicit def default[I <: String, T, A <: HList] = {
                at[List[( String, List[Any] )], Either[Error[I, A], T]] {
                    case ( raws, Right( _ ) )    ⇒ raws
                    case ( raws, Left( error ) ) ⇒ raws :+ error.raw
                }
            }
        }
    }
}