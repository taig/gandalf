package io.taig.bsts

import io.taig.bsts.ops.dsl.Operator
import io.taig.bsts.ops.hlist.NestedEvaluation
import shapeless.ops.hlist.{ LeftFolder, ToTraversable }
import shapeless._
import io.taig.bsts.syntax.raw._

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

    implicit def `Raw[Failure[Rule|Transformation]]`[N <: String, T, A <: HList](
        implicit
        r: Raw.Aux[Error[N, A], ( String, List[Any] )]
    ): Raw.Aux[Failure[Error[N, A], T], ( String, List[Any] )] = new Raw[Failure[Error[N, A], T]] {
        override type Out = ( String, List[Any] )

        override def raw( failure: Failure[Error[N, A], T] ): Out = failure.value.raw
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

        implicit def result[N <: String, T, A <: HList](
            implicit
            r: Raw.Aux[Error[N, A], ( String, List[Any] )]
        ) = at[List[( String, List[Any] )], Result[Error[N, A], T]] {
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

        implicit def coproduct[U <: HList, C <: HList](
            implicit
            lf: F[C]
        ) = at[List[( String, List[Any] )], Computed[C] :+: Unevaluated[U] :+: CNil] {
            case ( errors, Inl( Computed( tree ) ) ) ⇒ tree.foldLeft( errors )( this )
            case ( errors, _ )                       ⇒ errors
        }
    }
}