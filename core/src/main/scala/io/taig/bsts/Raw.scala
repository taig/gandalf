package io.taig.bsts

import cats.data.Validated.Valid
import cats.data.{ NonEmptyList, Validated }
import io.taig.bsts.ops.dsl.Operator
import io.taig.bsts.syntax.raw._
import shapeless._
import shapeless.ops.hlist.{ LeftFolder, ToTraversable }

trait Raw[-I, +O] {
    def raw( context: I ): O
}

object Raw {
    implicit def rawError[N <: String, A <: HList](
        implicit
        tt: ToTraversable.Aux[A, List, Any]
    ) = new Raw[Error[N, A], ( String, List[Any] )] {
        override def raw( error: Error[N, A] ) = ( error.name, error.arguments.toList )
    }

    implicit def rawTerm[N <: String, O, A <: HList](
        implicit
        r: Raw[Error[N, A], ( String, List[Any] )]
    ) = new Raw[Validated[Error[N, A], O], Validated[( String, List[Any] ), O]] {
        override def raw( result: Validated[Error[N, A], O] ) = result.leftMap( _.raw )
    }

    implicit def rawPolicy[C <: HList, T](
        implicit
        lf: collect.F[C]
    ) = new Raw[Validated[C, T], Validated[NonEmptyList[( String, List[Any] )], T]] {
        override def raw( validated: Validated[C, T] ) = {
            validated.leftMap { computation ⇒
                val list = computation.foldLeft( List.empty[( String, List[Any] )] )( collect )
                NonEmptyList( list.head, list.tail )
            }
        }
    }

    object collect extends Poly2 {
        type F[H <: HList] = LeftFolder.Aux[H, List[( String, List[Any] )], this.type, List[( String, List[Any] )]]

        implicit def term[N <: String, O, A <: HList](
            implicit
            r: Raw[Validated[Error[N, A], O], Validated[( String, List[Any] ), O]]
        ) = at[List[( String, List[Any] )], Validated[Error[N, A], O]] { ( errors, validated ) ⇒
            validated.raw.leftMap( errors :+ _ ).swap.getOrElse( errors )
        }

        implicit def valid[O] = at[List[( String, List[Any] )], Valid[O]] { ( errors, _ ) ⇒ errors }

        implicit def operator[O <: Operator] = at[List[( String, List[Any] )], O] { ( errors, _ ) ⇒ errors }

        implicit def recursion[L <: HList](
            implicit
            lf: F[L]
        ) = at[List[( String, List[Any] )], L] {
            case ( errors, tree ) ⇒ tree.foldLeft( errors )( this )
        }

        implicit def coproduct[U <: HList, C <: HList](
            implicit
            lf: F[C]
        ) = at[List[( String, List[Any] )], C :+: U :+: CNil] {
            case ( errors, Inl( tree ) ) ⇒ tree.foldLeft( errors )( this )
            case ( errors, _ )           ⇒ errors
        }
    }
}