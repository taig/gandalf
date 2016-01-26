package io.taig.bsts.ops

import io.taig.bsts.Operator.&
import io.taig.bsts._
import io.taig.bsts
import shapeless._

object hlist {
    trait Printer[L <: HList] extends DepFn1[L] with Serializable { type Out = String }

    object Printer extends Printer0 {
        def apply[L <: HList]( implicit printer: Printer[L] ): Printer[L] = printer

        implicit def hnilPrinter[C]: Printer[HNil] = new Printer[HNil] {
            override def apply( t: HNil ) = "HNil"
        }

        implicit def nestedHlistPrinter[H <: HList, T <: HList](
            implicit
            ph: Printer[H],
            pt: Printer[T]
        ): Printer[H :: T] = new Printer[H :: T] {
            override def apply( t: H :: T ): Out = s"(${ph( t.head )}) :: ${pt( t.tail )}"
        }
    }

    trait Printer0 {
        implicit def hconsPrinter[H, T <: HList](
            implicit
            p: Printer[T]
        ): Printer[H :: T] = new Printer[H :: T] {
            override def apply( t: H :: T ): Out = s"${t.head} :: ${p( t.tail )}"
        }
    }

    /**
     * Custom ConstMapper that respects nested HLists
     */
    trait NestedConstMapper[C, L <: HList] extends DepFn2[C, L] with Serializable { type Out <: HList }

    object NestedConstMapper extends NestedConstMapper0 {
        def apply[C, L <: HList]( implicit mapper: NestedConstMapper[C, L] ): Aux[C, L, mapper.Out] = mapper

        implicit def hnilNestedConstMapper[C]: Aux[C, HNil, HNil] = new NestedConstMapper[C, HNil] {
            type Out = HNil

            def apply( c: C, l: HNil ): Out = l
        }

        implicit def nestedHlistNestedConstMapper[H <: HList, T <: HList, C](
            implicit
            ncmh: NestedConstMapper[C, H],
            ncmt: NestedConstMapper[C, T]
        ): Aux[C, H :: T, ncmh.Out :: ncmt.Out] = new NestedConstMapper[C, H :: T] {
            type Out = ncmh.Out :: ncmt.Out

            def apply( c: C, l: H :: T ): Out = ncmh( c, l.head ) :: ncmt( c, l.tail )
        }
    }

    trait NestedConstMapper0 {
        type Aux[C, L <: HList, Out0 <: HList] = NestedConstMapper[C, L] { type Out = Out0 }

        implicit def hlistNestedConstMapper[H, T <: HList, C](
            implicit
            ncm: NestedConstMapper[C, T]
        ): Aux[C, H :: T, C :: ncm.Out] = new NestedConstMapper[C, H :: T] {
            type Out = C :: ncm.Out

            def apply( c: C, l: H :: T ): Out = c :: ncm( c, l.tail )
        }
    }

    trait NestedZip[A <: HList, B <: HList] extends DepFn2[A, B] with Serializable { type Out <: HList }

    object NestedZip extends NestedZip0 {
        def apply[A <: HList, B <: HList]( implicit zip: NestedZip[A, B] ): Aux[A, B, zip.Out] = zip

        implicit def hnilNestedZip: Aux[HNil, HNil, HNil] = new NestedZip[HNil, HNil] {
            override type Out = HNil

            override def apply( a: HNil, b: HNil ): Out = HNil
        }

        implicit def hlistNestedZip[H1 <: HList, T1 <: HList, H2 <: HList, T2 <: HList](
            implicit
            nzh: NestedZip[H1, H2],
            nzt: NestedZip[T1, T2]
        ): Aux[H1 :: T1, H2 :: T2, nzh.Out :: nzt.Out] = new NestedZip[H1 :: T1, H2 :: T2] {
            override type Out = nzh.Out :: nzt.Out

            override def apply( a: H1 :: T1, b: H2 :: T2 ): Out = nzh( a.head, b.head ) :: nzt( a.tail, b.tail )
        }

        implicit def operatorNestedZip[O <: Operator, T1 <: HList, H2, T2 <: HList](
            implicit
            nz: NestedZip[T1, T2]
        ): Aux[O :: T1, H2 :: T2, O :: nz.Out] = new NestedZip[O :: T1, H2 :: T2] {
            override type Out = O :: nz.Out

            override def apply( a: O :: T1, b: H2 :: T2 ): Out = a.head :: nz( a.tail, b.tail )
        }
    }

    trait NestedZip0 {
        type Aux[A <: HList, B <: HList, Out0 <: HList] = NestedZip[A, B] { type Out = Out0 }

        implicit def hconsNestedZip[H1, T1 <: HList, H2, T2 <: HList](
            implicit
            nz: NestedZip[T1, T2]
        ): Aux[H1 :: T1, H2 :: T2, ( H1, H2 ) :: nz.Out] = new NestedZip[H1 :: T1, H2 :: T2] {
            override type Out = ( H1, H2 ) :: nz.Out

            override def apply( a: H1 :: T1, b: H2 :: T2 ): Out = ( a.head, b.head ) :: nz( a.tail, b.tail )
        }
    }

    /**
     * Nested evaluation
     */
    trait NestedEvaluation[L <: HList] extends DepFn1[L] with Serializable {
        type Out0 <: HList

        type Out = ( Boolean, Computed[Out0] )
    }

    object NestedEvaluation extends NestedEvaluation0 {
        def apply[L <: HList]( implicit ne: NestedEvaluation[L] ): Aux[L, ne.Out0] = ne

        implicit def validateRule[I <: String, T, A <: HList] = {
            new NestedEvaluation[Rule[I, T, A]] {
                override type Out0 = Validation[I, T, A]

                override def apply( tree: Rule[I, T, A] ) = tree match {
                    case ( rule, value ) :: HNil ⇒
                        val validation = rule.validate( value )
                        ( validation.isSuccess, Computed( validation :: HNil ) )
                }
            }
        }

        implicit def operator[L <: HList, O <: Operator.Binary, R <: HList](
            implicit
            nel: NestedEvaluation[L],
            ner: NestedEvaluation[R],
            pr:  Printer[R]
        ) = {
            new NestedEvaluation[L :: O :: R] {
                override type Out0 = Computed[nel.Out0] :: O :: Either[Unevaluated[R], Computed[ner.Out0]] :: HNil

                override def apply( tree: L :: O :: R ) = tree match {
                    case l :: o :: r ⇒
                        val ( s1, left ) = nel( l )

                        if ( s1 ) {
                            ( true, Computed( left :: o :: Left( Unevaluated( r ) ) :: HNil ) )
                        } else {
                            val ( s2, res ) = ner( r )
                            ( s2, Computed( left :: o :: Right( res ) :: HNil ) )
                        }
                }
            }
        }
    }

    trait NestedEvaluation0 extends NestedEvaluation1 {
        implicit def stepIn[H <: HList, T <: HList, O <: HList](
            implicit
            neh: NestedEvaluation.Aux[H, O],
            p:   Printer[Computed[O] :: T]
        ) = {
            new NestedEvaluation[H :: T] {
                override type Out0 = Computed[O] :: T

                override def apply( tree: H :: T ) = {
                    val ( state, lhs ) = neh( tree.head )
                    ( state, Computed( lhs :: tree.tail ) )
                }
            }
        }
    }

    trait NestedEvaluation1 {
        type Aux[L <: HList, Out1 <: HList] = NestedEvaluation[L] { type Out0 = Out1 }

        type Rule[I <: String, T, A <: HList] = ( bsts.Rule[I, T, A], T ) :: HNil
        type Validation[I <: String, T, A <: HList] = bsts.Validation[Error[I, A], T] :: HNil

        var indent = 0
    }
}