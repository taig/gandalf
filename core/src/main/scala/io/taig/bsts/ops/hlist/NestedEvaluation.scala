package io.taig.bsts.ops.hlist

import io.taig.bsts.Rule.Chain1
import io.taig.bsts._
import io.taig.bsts.ops.dsl.Operator
import shapeless._

trait NestedEvaluation[I, O, -T <: HList] extends Serializable {
    type Out0 <: HList

    type Out = ( Option[O], Computed[Out0] )

    def apply( input: I, tree: T ): Out
}

object NestedEvaluation extends NestedEvaluation0 {
    def apply[I, O, L <: HList]( implicit ne: NestedEvaluation[I, O, L] ): Aux[I, O, L, ne.Out0] = ne

    implicit def hnil[I] = new NestedEvaluation[I, I, HNil] {
        override type Out0 = HNil

        override def apply( input: I, tree: HNil ) = ( Some( input ), Computed( HNil ) )
    }

    implicit def rule[N <: String, T, A <: HList] = new NestedEvaluation[T, T, Rule[N, T, A] :: HNil] {
        override type Out0 = Result[Error[N, A], T] :: HNil

        override def apply( input: T, tree: Rule[N, T, A] :: HNil ) = tree match {
            case rule :: HNil ⇒ rule.validate( input ) match {
                case s @ Success( output ) ⇒ ( Some( output ), Computed( s :: HNil ) )
                case f @ Failure( _ )      ⇒ ( None, Computed( f :: HNil ) )
            }
        }
    }

    implicit def transformation[N <: String, I, O, A <: HList] = {
        new NestedEvaluation[I, O, Transformation[N, I, O, A] :: HNil] {
            override type Out0 = Result[Error[N, A], O] :: HNil

            override def apply( input: I, tree: Transformation[N, I, O, A] :: HNil ) = tree match {
                case transformation :: HNil ⇒ transformation.transform( input ) match {
                    case s @ Success( output ) ⇒ ( Some( output ), Computed( s :: HNil ) )
                    case f @ Failure( _ )      ⇒ ( None, Computed( f :: HNil ) )
                }
            }
        }
    }

    implicit def operationT[I, O, P, L <: HList, R <: HList](
        implicit
        nel: NestedEvaluation[I, O, L],
        ner: NestedEvaluation[O, P, R],
        p:   Printer[R]
    ) = new NestedEvaluation[I, P, L :: Operator.~>.type :: R] {
        type C = Computed[ner.Out0] :+: Unevaluated[R] :+: CNil

        override type Out0 = Computed[nel.Out0] :: Operator.~>.type :: C :: HNil

        override def apply( input: I, tree: L :: Operator.~>.type :: R ) = tree match {
            case l :: o :: r ⇒
                nel( input, l ) match {
                    case ( None, lhs ) ⇒ ( None, Computed( lhs :: o :: Coproduct[C]( Unevaluated( r ) ) :: HNil ) )
                    case ( Some( output ), lhs ) ⇒ ner( output, r ) match {
                        case ( output, rhs ) ⇒ ( output, Computed( lhs :: o :: Coproduct[C]( rhs ) :: HNil ) )
                    }
                }
        }
    }

    implicit def operationR[T, L <: HList, OP <: Operator.Binary, R <: HList](
        implicit
        nel: NestedEvaluation[T, T, L],
        ner: NestedEvaluation[T, T, R],
        p:   Printer[R]
    ) = new NestedEvaluation[T, T, L :: OP :: R] {
        type C = Computed[ner.Out0] :+: Unevaluated[R] :+: CNil

        override type Out0 = Computed[nel.Out0] :: OP :: C :: HNil

        override def apply( input: T, tree: L :: OP :: R ) = tree match {
            case l :: Operator.& :: r ⇒ ( nel( input, l ), ner( input, r ) ) match {
                case ( ( a, lhs ), ( b, rhs ) ) ⇒
                    ( a.flatMap( _ ⇒ b ), Computed( lhs :: Operator.& :: Coproduct[C]( rhs ) :: HNil ) )
            }
            case l :: Operator.&& :: r ⇒ nel( input, l ) match {
                case ( None, lhs ) ⇒
                    ( None, Computed( lhs :: Operator.&& :: Coproduct[C]( Unevaluated( r ) ) :: HNil ) )
                case ( Some( _ ), lhs ) ⇒ ner( input, r ) match {
                    case ( output, rhs ) ⇒ ( output, Computed( lhs :: Operator.&& :: Coproduct[C]( rhs ) :: HNil ) )
                }
            }
            case l :: Operator.| :: r ⇒ ( nel( input, l ), ner( input, r ) ) match {
                case ( ( a, lhs ), ( b, rhs ) ) ⇒
                    ( a orElse b, Computed( lhs :: Operator.| :: Coproduct[C]( rhs ) :: HNil ) )
            }
            case l :: Operator.|| :: r ⇒ nel( input, l ) match {
                case ( s @ Some( _ ), lhs ) ⇒
                    ( s, Computed( lhs :: Operator.|| :: Coproduct[C]( Unevaluated( r ) ) :: HNil ) )
                case ( None, lhs ) ⇒ ner( input, r ) match {
                    case ( b, rhs ) ⇒ ( b, Computed( lhs :: Operator.|| :: Coproduct[C]( rhs ) :: HNil ) )
                }
            }
            case l :: Operator.^ :: r ⇒ ( nel( input, l ), ner( input, r ) ) match {
                case ( ( Some( _ ), lhs ), ( None, rhs ) ) ⇒
                    ( Some( input ), Computed( lhs :: Operator.^ :: Coproduct[C]( rhs ) :: HNil ) )
                case ( ( None, lhs ), ( Some( _ ), rhs ) ) ⇒
                    ( Some( input ), Computed( lhs :: Operator.^ :: Coproduct[C]( rhs ) :: HNil ) )
                case ( ( _, lhs ), ( _, rhs ) ) ⇒
                    ( None, Computed( lhs :: Operator.^ :: Coproduct[C]( rhs ) :: HNil ) )
            }
        }
    }

}

trait NestedEvaluation0 {
    type Aux[I, O, L <: HList, O0 <: HList] = NestedEvaluation[I, O, L] { type Out0 = O0 }

    implicit def recursion[I, O, P, H <: HList, T <: HList](
        implicit
        neh: NestedEvaluation[I, O, H],
        net: NestedEvaluation[O, P, T],
        p:   Printer[T]
    ) = new NestedEvaluation[I, P, H :: T] {
        type C = ( Computed[net.Out0] :+: Unevaluated[T] :+: CNil )

        override type Out0 = Computed[neh.Out0] :: C :: HNil

        override def apply( input: I, tree: H :: T ) = tree match {
            case head :: tail ⇒ neh( input, head ) match {
                case ( Some( output ), lhs ) ⇒ net( output, tail ) match {
                    case ( output, rhs ) ⇒ ( output, Computed( lhs :: Coproduct[C]( rhs ) :: HNil ) )
                }
                case ( None, lhs ) ⇒ ( None, Computed( lhs :: Coproduct[C]( Unevaluated( tail ) ) :: HNil ) )
            }
        }
    }
}

trait Yolo {
    implicit def yolo[I, O, L <: HList](
        implicit
        p: Printer[L]
    ) = new NestedEvaluation[I, O, L] {
        override type Out0 = L

        override def apply( t: I, u: L ) = {
            println( "Yolor: " + u )
            ( None, Computed( u ) )
        }
    }
}