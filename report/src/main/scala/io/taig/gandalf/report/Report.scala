package io.taig.gandalf.report

import cats.data.Validated.{ Invalid, Valid }
import cats.data.{ NonEmptyList, Validated }
import io.taig.gandalf._
import io.taig.gandalf.ops.dsl.Operator
import io.taig.gandalf.report.syntax.report._
import shapeless._
import shapeless.ops.hlist.LeftFolder

trait Report[-I, O] {
    def report( input: I ): NonEmptyList[O]
}

object Report {
    def instance[N <: String, A <: HList, O]( f: A ⇒ O ): Report[Error[N, A], O] = {
        new Report[Error[N, A], O] {
            override def report( error: Error[N, A] ) = NonEmptyList.of( f( error.arguments ) )
        }
    }

    /**
     * Construct a Report for a Rule
     *
     * {{{
     * Report( rule.required )( _ => "required" )
     * Report( rule.min )( args => s"min ${args("expected")}" )
     * }}}
     */
    def apply[N <: String, A <: HList, O]( term: Term[N, _, _, A] )( message: A ⇒ O ): Report[Error[N, A], O] = {
        instance( message )
    }

    def apply[N <: String, A <: HList, O]( term: ( _ ) ⇒ Term[N, _, _, A] )(
        message: A ⇒ O
    ): Report[Error[N, A], O] = instance( message )

    def apply[N <: String, A <: HList, O]( term: ( _, _ ) ⇒ Term[N, _, _, A] )(
        message: A ⇒ O
    ): Report[Error[N, A], O] = instance( message )

    def apply[N <: String, A <: HList, O]( term: ( _, _, _ ) ⇒ Term[N, _, _, A] )(
        message: A ⇒ O
    ): Report[Error[N, A], O] = instance( message )

    def apply[N <: String, A <: HList, O]( term: ( _, _, _, _ ) ⇒ Term[N, _, _, A] )(
        message: A ⇒ O
    ): Report[Error[N, A], O] = instance( message )

    implicit def reportComputation[C <: HList, O](
        implicit
        lf: collect.F[C, O]
    ): Report[C, O] = new Report[C, O] {
        override def report( computation: C ) = {
            val list = computation.foldLeft( List.empty[O] )( collect )
            NonEmptyList.of( list.head, list.tail: _* )
        }
    }

    object collect extends Poly2 {
        type F[H <: HList, O] = LeftFolder.Aux[H, List[O], this.type, List[O]]

        implicit def term[N <: String, O, A <: HList, P](
            implicit
            r: Report[Error[N, A], P]
        ): Case.Aux[List[P], Validated[Error[N, A], O], List[P]] = at { ( errors, validated ) ⇒
            import cats.instances.list._
            errors ++ validated.leftMap( _.report.toList ).swap.getOrElse( Nil )
        }

        implicit def reportableError[N <: String, O, A <: HList, P]: Case.Aux[List[P], Validated[ReportableError[N, A, P], O], List[P]] = {
            at { ( errors, validated ) ⇒
                import cats.instances.list._
                errors ++ validated.leftMap( _.report.toList ).swap.getOrElse( Nil )
            }
        }

        implicit def valid[O, P]: Case.Aux[List[P], Valid[O], List[P]] = at { ( errors, _ ) ⇒ errors }

        implicit def operator[O <: Operator, P]: Case.Aux[List[P], O, List[P]] = at { ( errors, _ ) ⇒ errors }

        implicit def recursion[L <: HList, O](
            implicit
            lf: F[L, O]
        ): Case.Aux[List[O], L, List[O]] = at { case ( errors, tree ) ⇒ tree.foldLeft( errors )( this ) }

        implicit def coproduct[U <: HList, C <: HList, O](
            implicit
            lf: F[C, O]
        ): Case.Aux[List[O], C :+: U :+: CNil, List[O]] = at {
            case ( errors, Inl( tree ) ) ⇒ tree.foldLeft( errors )( this )
            case ( errors, _ )           ⇒ errors
        }
    }
}