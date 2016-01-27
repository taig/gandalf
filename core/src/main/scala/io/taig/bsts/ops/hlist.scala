package io.taig.bsts.ops

import io.taig.bsts
import io.taig.bsts._
import shapeless._

object hlist {
    trait Printer[L <: HList] extends DepFn1[L] with Serializable { type Out = String }

    object Printer extends Printer0 {
        def apply[L <: HList]( implicit p: Printer[L] ): Printer[L] = p

        implicit def hnil: Printer[HNil] = new Printer[HNil] {
            override def apply( t: HNil ) = "HNil"
        }

        implicit def recursion[H <: HList, T <: HList](
            implicit
            ph: Printer[H],
            pt: Printer[T]
        ): Printer[H :: T] = new Printer[H :: T] {
            override def apply( t: H :: T ): Out = s"(${ph( t.head )}) :: ${pt( t.tail )}"
        }
    }

    trait Printer0 {
        implicit def hcons[H, T <: HList](
            implicit
            p: Printer[T]
        ): Printer[H :: T] = new Printer[H :: T] {
            override def apply( t: H :: T ): Out = s"${t.head} :: ${p( t.tail )}"
        }
    }

    trait LogicalExpressionTreePrinter[L <: HList] extends DepFn1[L] with Serializable { type Out = String }

    object LogicalExpressionTreePrinter extends LogicalExpressionTreePrinter0 {
        def apply[L <: HList]( implicit letp: LogicalExpressionTreePrinter[L] ): LogicalExpressionTreePrinter[L] = letp

        implicit def hnil: LogicalExpressionTreePrinter[HNil] = new LogicalExpressionTreePrinter[HNil] {
            override def apply( t: HNil ): Out = ""
        }

        implicit def operationSimple[L <: Rule[_, _, _], O <: Operator.Binary, R <: Rule[_, _, _]] = {
            new LogicalExpressionTreePrinter[( L :: HNil ) :: O :: ( R :: HNil ) :: HNil] {
                override def apply( tree: ( L :: HNil ) :: O :: ( R :: HNil ) :: HNil ) = tree match {
                    case ( l :: HNil ) :: o :: ( r :: HNil ) :: HNil ⇒ s"$l $o $r"
                }
            }
        }

        implicit def operationLeft[L <: HList, O <: Operator.Binary, R <: Rule[_, _, _]](
            implicit
            letp: LogicalExpressionTreePrinter[L]
        ) = {
            new LogicalExpressionTreePrinter[L :: O :: ( R :: HNil ) :: HNil] {
                override def apply( tree: L :: O :: ( R :: HNil ) :: HNil ) = tree match {
                    case l :: o :: ( r :: HNil ) :: HNil ⇒ s"(${letp( l )}) $o $r"
                }
            }
        }

        implicit def operationRight[L <: Rule[_, _, _], O <: Operator.Binary, R <: HList](
            implicit
            letp: LogicalExpressionTreePrinter[R]
        ) = {
            new LogicalExpressionTreePrinter[( L :: HNil ) :: O :: R :: HNil] {
                override def apply( tree: ( L :: HNil ) :: O :: R :: HNil ) = tree match {
                    case ( l :: HNil ) :: o :: r :: HNil ⇒ s"$l $o (${letp( r )})"
                }
            }
        }

        implicit def operationDeep[L <: HList, O <: Operator.Binary, R <: HList](
            implicit
            letpl: LogicalExpressionTreePrinter[L],
            letpr: LogicalExpressionTreePrinter[R]
        ): LogicalExpressionTreePrinter[L :: O :: R] = {
            new LogicalExpressionTreePrinter[L :: O :: R] {
                override def apply( tree: L :: O :: R ) = tree match {
                    case l :: o :: r ⇒ s"(${letpl( l )}) $o (${letpr( r )})"
                }
            }
        }
    }

    trait LogicalExpressionTreePrinter0 extends LogicalExpressionTreePrinter1 {
        implicit def recursion[L <: HList, R <: HList](
            implicit
            letpl: LogicalExpressionTreePrinter[L],
            letpr: LogicalExpressionTreePrinter[R]
        ): LogicalExpressionTreePrinter[L :: R] = new LogicalExpressionTreePrinter[L :: R] {
            override def apply( tree: L :: R ) = s"${letpl( tree.head )}${letpr( tree.tail )}"
        }
    }

    trait LogicalExpressionTreePrinter1 {
        implicit def hcons[H, T <: HNil](
            implicit
            letp: LogicalExpressionTreePrinter[T]
        ): LogicalExpressionTreePrinter[H :: T] = {
            new LogicalExpressionTreePrinter[H :: T] {
                override def apply( tree: H :: T ): Out = s"${tree.head}${letp( tree.tail )}"
            }
        }
    }

    /**
     * Custom ConstMapper that respects nested HLists
     */
    trait NestedConstMapper[C, L <: HList] extends DepFn2[C, L] with Serializable { type Out <: HList }

    object NestedConstMapper extends NestedConstMapper0 {
        def apply[C, L <: HList]( implicit mapper: NestedConstMapper[C, L] ): Aux[C, L, mapper.Out] = mapper

        implicit def hnil[C]: Aux[C, HNil, HNil] = new NestedConstMapper[C, HNil] {
            type Out = HNil

            def apply( c: C, l: HNil ): Out = l
        }

        implicit def recursion[H <: HList, T <: HList, C](
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

        implicit def hcons[H, T <: HList, C](
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

        implicit def hnil: Aux[HNil, HNil, HNil] = new NestedZip[HNil, HNil] {
            override type Out = HNil

            override def apply( a: HNil, b: HNil ): Out = HNil
        }

        implicit def recursion[H1 <: HList, T1 <: HList, H2 <: HList, T2 <: HList](
            implicit
            nzh: NestedZip[H1, H2],
            nzt: NestedZip[T1, T2]
        ): Aux[H1 :: T1, H2 :: T2, nzh.Out :: nzt.Out] = new NestedZip[H1 :: T1, H2 :: T2] {
            override type Out = nzh.Out :: nzt.Out

            override def apply( a: H1 :: T1, b: H2 :: T2 ): Out = nzh( a.head, b.head ) :: nzt( a.tail, b.tail )
        }

        implicit def operator[O <: Operator, T1 <: HList, H2, T2 <: HList](
            implicit
            nz: NestedZip[T1, T2]
        ): Aux[O :: T1, H2 :: T2, O :: nz.Out] = new NestedZip[O :: T1, H2 :: T2] {
            override type Out = O :: nz.Out

            override def apply( a: O :: T1, b: H2 :: T2 ): Out = a.head :: nz( a.tail, b.tail )
        }
    }

    trait NestedZip0 {
        type Aux[A <: HList, B <: HList, Out0 <: HList] = NestedZip[A, B] { type Out = Out0 }

        implicit def hcons[H1, T1 <: HList, H2, T2 <: HList](
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

        implicit def rule[I <: String, T, A <: HList] = {
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
        ): Aux[L :: O :: R, Computed[nel.Out0] :: O :: Either[Unevaluated[R], Computed[ner.Out0]] :: HNil] = {
            new NestedEvaluation[L :: O :: R] {
                override type Out0 = Computed[nel.Out0] :: O :: Either[Unevaluated[R], Computed[ner.Out0]] :: HNil

                override def apply( tree: L :: O :: R ): ( Boolean, Computed[Out0] ) = {
                    val ( state, left, right ) = tree match {
                        case l :: Operator.& :: r ⇒ ( nel( l ), ner( r ) ) match {
                            case ( ( a, l ), ( b, r ) ) ⇒ ( a && b, l, Right( r ) )
                        }
                        case l :: Operator.&& :: r ⇒ nel( l ) match {
                            case ( false, l ) ⇒ ( false, l, Left( Unevaluated( r ) ) )
                            case ( true, l ) ⇒ ner( r ) match {
                                case ( a, r ) ⇒ ( a, l, Right( r ) )
                            }
                        }
                        case l :: Operator.| :: r ⇒ ( nel( l ), ner( r ) ) match {
                            case ( ( a, l ), ( b, r ) ) ⇒ ( a || b, l, Right( r ) )
                        }
                        case l :: Operator.|| :: r ⇒ nel( l ) match {
                            case ( true, l ) ⇒ ( true, l, Left( Unevaluated( r ) ) )
                            case ( false, l ) ⇒ ner( r ) match {
                                case ( b, r ) ⇒ ( b, l, Right( r ) )
                            }
                        }
                        case l :: Operator.^ :: r ⇒ ( nel( l ), ner( r ) ) match {
                            case ( ( a, l ), ( b, r ) ) ⇒ ( a != b, l, Right( r ) )
                        }
                    }

                    ( state, Computed( left :: tree.tail.head :: right :: HNil ) )
                }
            }
        }
    }

    trait NestedEvaluation0 {
        type Aux[L <: HList, Out1 <: HList] = NestedEvaluation[L] { type Out0 = Out1 }

        protected type Rule[I <: String, T, A <: HList] = ( bsts.Rule[I, T, A], T ) :: HNil
        protected type Validation[I <: String, T, A <: HList] = bsts.Validation[Error[I, A], T] :: HNil

        implicit def recursion[H <: HList, T <: HList, O <: HList](
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
}