package io.taig.gandalf

import cats.data.Validated.Valid
import cats.data.{ NonEmptyList, Validated }
import io.taig.gandalf.ops.dsl.Operator
import shapeless._
import shapeless.ops.hlist.LeftFolder

/**
 * Typeclass that provides an untyped error output for failed validations
 *
 * The output format is always a `NonEmptyList[( String, List[Any] )]`
 *
 * @tparam T Error type to represent
 */
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

        implicit def validated[E, A](
            implicit
            r: Raw[E]
        ): Case.Aux[List[( String, List[Any] )], Validated[E, A], List[( String, List[Any] )]] = {
            at { ( errors, validated ) ⇒
                import cats.std.list._
                errors ++ validated.leftMap( e ⇒ r.raw( e ).unwrap ).swap.getOrElse( Nil )
            }
        }

        implicit def valid[O]: Case.Aux[List[( String, List[Any] )], Valid[O], List[( String, List[Any] )]] = {
            at { ( errors, _ ) ⇒ errors }
        }

        implicit def operator[O <: Operator]: Case.Aux[List[( String, List[Any] )], O, List[( String, List[Any] )]] = {
            at { ( errors, _ ) ⇒ errors }
        }

        implicit def recursion[L <: HList](
            implicit
            lf: F[L]
        ): Case.Aux[List[( String, List[Any] )], L, List[( String, List[Any] )]] = at {
            case ( errors, tree ) ⇒ tree.foldLeft( errors )( this )
        }

        implicit def coproduct[U <: HList, C <: HList](
            implicit
            lf: F[C]
        ): Case.Aux[List[( String, List[Any] )], C :+: U :+: CNil, List[( String, List[Any] )]] = at {
            case ( errors, Inl( tree ) ) ⇒ tree.foldLeft( errors )( this )
            case ( errors, _ )           ⇒ errors
        }
    }
}