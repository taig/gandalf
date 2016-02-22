package io.taig.bsts

import cats.data.Validated.Valid
import cats.data.{ NonEmptyList, Validated }
import io.taig.bsts.ops.dsl.Operator
import io.taig.bsts.syntax.raw._
import shapeless._
import shapeless.ops.hlist.LeftFolder

trait Raw[-T] {
    def raw( value: T ): NonEmptyList[( String, List[Any] )]
}

object Raw {
    implicit def rawError[N <: String, A <: HList] = new Raw[Error[N, A]] {
        override def raw( error: Error[N, A] ) = NonEmptyList( ( error.name, error.arguments.runtimeList ) )
    }

    implicit def rawComputation[C <: HList](
        implicit
        lf: collect.F[C]
    ) = new Raw[C] {
        override def raw( computation: C ) = {
            val list = computation.foldLeft( List.empty[( String, List[Any] )] )( collect )
            NonEmptyList( list.head, list.tail )
        }
    }

    object collect extends Poly2 {
        type F[H <: HList] = LeftFolder.Aux[H, List[( String, List[Any] )], this.type, List[( String, List[Any] )]]

        implicit def term[N <: String, O, A <: HList](
            implicit
            r: Raw[Error[N, A]]
        ) = at[List[( String, List[Any] )], Validated[Error[N, A], O]] { ( errors, validated ) ⇒
            import cats.std.list._
            errors ++ validated.leftMap( _.raw.unwrap ).swap.getOrElse( Nil )
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