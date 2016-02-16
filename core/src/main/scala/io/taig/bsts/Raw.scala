package io.taig.bsts

import cats.data.Validated.Invalid
import cats.data.{ NonEmptyList, Validated }
import io.taig.bsts.ops.{ Computed, Unevaluated }
import io.taig.bsts.syntax.raw._
import shapeless._
import shapeless.ops.hlist.{ LeftFolder, ToTraversable }

trait Raw[-I, +O] {
    def raw( context: I ): O
}

object Raw {
    implicit def `Raw[Error]`[N <: String, A <: HList](
        implicit
        tt: ToTraversable.Aux[A, List, Any]
    ) = new Raw[Error[N, A], ( String, List[Any] )] {
        override def raw( error: Error[N, A] ) = ( error.name, error.arguments.toList )
    }

    implicit def `Raw[Term]`[N <: String, O, A <: HList](
        implicit
        r: Raw[Error[N, A], ( String, List[Any] )]
    ) = new Raw[Validated[Error[N, A], O], Validated[( String, List[Any] ), O]] {
        override def raw( result: Validated[Error[N, A], O] ) = result.leftMap( _.raw )
    }

    implicit def `Raw[Policy]`[C <: HList, T](
        implicit
        lf: collect.F[C]
    ) = new Raw[Validated[Computed[C], T], Validated[NonEmptyList[( String, List[Any] )], T]] {
        override def raw( validated: Validated[Computed[C], T] ) = {
            validated.leftMap { computation ⇒
                val list = computation.tree.foldLeft( List.empty[( String, List[Any] )] )( collect )
                NonEmptyList( list.head, list.tail )
            }
        }
    }

    object collect extends collect0 {
        type F[H <: HList] = LeftFolder.Aux[H, List[( String, List[Any] )], this.type, List[( String, List[Any] )]]

        implicit def validated[N <: String, O, A <: HList](
            implicit
            r: Raw[Error[N, A], ( String, List[Any] )]
        ) = at[List[( String, List[Any] )], Validated[Error[N, A], O]] {
            case ( errors, Invalid( error ) ) ⇒ errors :+ error.raw
            case ( errors, _ )                ⇒ errors
        }

        implicit def computed[L <: HList](
            implicit
            lf: F[L]
        ) = at[List[( String, List[Any] )], Computed[L]] {
            case ( errors, Computed( tree ) ) ⇒ tree.foldLeft( errors )( this )
        }

        implicit def coproduct[U <: HList, C <: HList](
            implicit
            lf: F[C]
        ) = at[List[( String, List[Any] )], Computed[C] :+: Unevaluated[U] :+: CNil] {
            case ( errors, Inl( Computed( tree ) ) ) ⇒ tree.foldLeft( errors )( this )
            case ( errors, _ )                       ⇒ errors
        }
    }

    trait collect0 extends Poly2 {
        implicit def identity[T] = at[List[( String, List[Any] )], T]( ( errors, _ ) ⇒ errors )
    }
}