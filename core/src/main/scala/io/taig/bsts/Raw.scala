package io.taig.bsts

import cats.data.Xor._
import cats.data.{ NonEmptyList, Xor }
import io.taig.bsts.ops.{ Computed, Unevaluated }
import io.taig.bsts.syntax.raw._
import shapeless._
import shapeless.ops.hlist.{ LeftFolder, ToTraversable }

trait Raw[-T] {
    type Out

    def raw( context: T ): Out
}

object Raw {
    type Aux[T, Out0] = Raw[T] { type Out = Out0 }

    implicit def `Raw[Error]`[N <: String, A <: HList](
        implicit
        w:  Witness.Aux[N],
        tt: ToTraversable.Aux[A, List, Any]
    ): Raw.Aux[Error[N, A], ( String, List[Any] )] = new Raw[Error[N, A]] {
        override type Out = ( String, List[Any] )

        override def raw( error: Error[N, A] ): Out = ( w.value, error.arguments.toList )
    }

    implicit def `Raw[Term]`[N <: String, T, A <: HList](
        implicit
        r: Raw.Aux[Error[N, A], ( String, List[Any] )]
    ): Raw.Aux[Xor[Error[N, A], T], Xor[( String, List[Any] ), T]] = new Raw[Xor[Error[N, A], T]] {
        override type Out = Xor[( String, List[Any] ), T]

        override def raw( result: Xor[Error[N, A], T] ): Out = result.leftMap( _.raw )
    }

    implicit def `Raw[Policy]`[C <: HList, T](
        implicit
        lf: collect.F[C]
    ): Raw.Aux[Xor[Computed[C], T], Xor[NonEmptyList[( String, List[Any] )], T]] = {
        new Raw[Xor[Computed[C], T]] {
            override type Out = Xor[NonEmptyList[( String, List[Any] )], T]

            override def raw( validated: Xor[Computed[C], T] ): Out = {
                validated.leftMap { computation ⇒
                    val list = computation.tree.foldLeft( List.empty[( String, List[Any] )] )( collect )
                    NonEmptyList( list.head, list.tail )
                }
            }
        }
    }

    object collect extends collect0 {
        type F[H <: HList] = LeftFolder.Aux[H, List[( String, List[Any] )], this.type, List[( String, List[Any] )]]

        implicit def validated[N <: String, T, A <: HList](
            implicit
            r: Raw.Aux[Error[N, A], ( String, List[Any] )]
        ) = at[List[( String, List[Any] )], Xor[Error[N, A], T]] {
            case ( errors, Left( error ) ) ⇒ errors :+ error.raw
            case ( errors, _ )             ⇒ errors
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