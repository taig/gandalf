package io.taig.bsts

import export.{ imports, exports }
import io.taig.bsts.syntax.raw._
import shapeless.ops.hlist.{ LeftFolder, ToTraversable }
import shapeless.{ Poly2, Witness, HList }

trait Raw[T] {
    type Out

    def raw( context: T ): Out
}

@imports[Raw]
object Raw {
    type Aux[T, Out0] = Raw[T] { type Out = Out0 }

    implicit def `Raw[Error]`[I <: String, A <: HList](
        implicit
        w:  Witness.Aux[I],
        tt: ToTraversable.Aux[A, List, Any]
    ): Raw.Aux[Error[I, A], ( String, List[Any] )] = new Raw[Error[I, A]] {
        override type Out = ( String, List[Any] )

        override def raw( error: Error[I, A] ): Out = ( w.value, error.arguments.toList )
    }

    implicit def `Raw[Failure[Rule]]`[I <: String, T, A <: HList](
        implicit
        r: Raw.Aux[Error[I, A], ( String, List[Any] )]
    ): Raw.Aux[Failure[Error[I, A], T], ( String, List[Any] )] = new Raw[Failure[Error[I, A], T]] {
        override type Out = ( String, List[Any] )

        override def raw( failure: Failure[Error[I, A], T] ): Out = failure.value.raw
    }

    implicit def `Raw[Failure[Policy]]`[C <: HList, T](
        implicit
        lf: collect.F[C]
    ): Raw.Aux[Failure[Computed[C], T], lf.Out] = new Raw[Failure[Computed[C], T]] {
        override type Out = lf.Out

        override def raw( failure: Failure[Computed[C], T] ): Out = {
            failure.value.tree.foldLeft( List.empty[( String, List[Any] )] )( collect )
        }
    }

    object collect extends Poly2 {
        type F[H <: HList] = LeftFolder.Aux[H, List[( String, List[Any] )], this.type, List[( String, List[Any] )]]

        implicit def validation[I <: String, T, A <: HList](
            implicit
            r: Raw.Aux[Error[I, A], ( String, List[Any] )]
        ) = at[List[( String, List[Any] )], Validation[Error[I, A], T]] {
            case ( errors, Failure( error ) ) ⇒ errors :+ error.raw
            case ( errors, _ )                ⇒ errors
        }

        implicit def operator[O <: Operator.Binary] = at[List[( String, List[Any] )], O]( ( errors, _ ) ⇒ errors )

        implicit def computed[L <: HList](
            implicit
            lf: F[L]
        ) = at[List[( String, List[Any] )], Computed[L]] {
            case ( errors, Computed( tree ) ) ⇒ tree.foldLeft( errors )( this )
        }

        implicit def either[U <: HList, C <: HList](
            implicit
            lf: F[C]
        ) = at[List[( String, List[Any] )], Either[Unevaluated[U], Computed[C]]] {
            case ( errors, Right( Computed( tree ) ) ) ⇒ tree.foldLeft( errors )( this )
            case ( errors, Left( Unevaluated( _ ) ) )  ⇒ errors
        }
    }

    trait test extends Poly2 {
        implicit def default[T] = at[List[( String, List[Any] )], T] { ( l, r ) ⇒
            println( "lp: " + r )
            l
        }
    }
}