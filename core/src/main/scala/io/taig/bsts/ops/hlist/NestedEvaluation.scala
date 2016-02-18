package io.taig.bsts.ops.hlist

import io.taig.bsts.ops.dsl.Operator
import shapeless._

trait NestedEvaluation[I, O, -T <: HList] extends Serializable {
    type Out0 <: HList

    type Out = ( Option[O], Out0 )

    def apply( input: I, tree: T ): Out
}

object NestedEvaluation extends NestedEvaluation0 {
    def apply[I, O, L <: HList]( implicit ne: NestedEvaluation[I, O, L] ): Aux[I, O, L, ne.Out0] = ne

    implicit def hnil[I] = new NestedEvaluation[I, I, HNil] {
        override type Out0 = HNil

        override def apply( input: I, tree: HNil ) = ( Some( input ), HNil )
    }

    implicit def transformation[I, O, P, L <: HList, R <: HList](
        implicit
        nel: NestedEvaluation[I, O, L],
        ner: NestedEvaluation[O, P, R]
    ) = new NestedEvaluation[I, P, L :: Operator.~>.type :: R] {
        type C = ner.Out0 :+: R :+: CNil

        override type Out0 = nel.Out0 :: Operator.~>.type :: C :: HNil

        override def apply( input: I, tree: L :: Operator.~>.type :: R ) = tree match {
            case l :: o :: r ⇒ nel( input, l ) match {
                case ( None, lhs ) ⇒ ( None, lhs :: o :: Coproduct[C]( r ) :: HNil )
                case ( Some( output ), lhs ) ⇒ ner( output, r ) match {
                    case ( output, rhs ) ⇒ ( output, lhs :: o :: Coproduct[C]( rhs ) :: HNil )
                }
            }
        }
    }
}

trait NestedEvaluation0 extends NestedEvaluation1 {
    implicit def term[T, L <: HList, O <: Operator.Binary, R <: HList](
        implicit
        nel: NestedEvaluation[T, T, L],
        ner: NestedEvaluation[T, T, R]
    ) = new NestedEvaluation[T, T, L :: O :: R] {
        type C = ner.Out0 :+: R :+: CNil

        override type Out0 = nel.Out0 :: O :: C :: HNil

        override def apply( input: T, tree: L :: O :: R ) = tree match {
            case l :: Operator.& :: r ⇒ ( nel( input, l ), ner( input, r ) ) match {
                case ( ( a, lhs ), ( b, rhs ) ) ⇒
                    ( a.flatMap( _ ⇒ b ), lhs :: Operator.& :: Coproduct[C]( rhs ) :: HNil )
            }
            case l :: Operator.&& :: r ⇒ nel( input, l ) match {
                case ( None, lhs ) ⇒
                    ( None, lhs :: Operator.&& :: Coproduct[C]( r ) :: HNil )
                case ( Some( _ ), lhs ) ⇒ ner( input, r ) match {
                    case ( output, rhs ) ⇒ ( output, lhs :: Operator.&& :: Coproduct[C]( rhs ) :: HNil )
                }
            }
            case l :: Operator.| :: r ⇒ ( nel( input, l ), ner( input, r ) ) match {
                case ( ( a, lhs ), ( b, rhs ) ) ⇒
                    ( a orElse b, lhs :: Operator.| :: Coproduct[C]( rhs ) :: HNil )
            }
            case l :: Operator.|| :: r ⇒ nel( input, l ) match {
                case ( s @ Some( _ ), lhs ) ⇒
                    ( s, lhs :: Operator.|| :: Coproduct[C]( r ) :: HNil )
                case ( None, lhs ) ⇒ ner( input, r ) match {
                    case ( b, rhs ) ⇒ ( b, lhs :: Operator.|| :: Coproduct[C]( rhs ) :: HNil )
                }
            }
            case l :: Operator.^ :: r ⇒ ( nel( input, l ), ner( input, r ) ) match {
                case ( ( Some( _ ), lhs ), ( None, rhs ) ) ⇒
                    ( Some( input ), lhs :: Operator.^ :: Coproduct[C]( rhs ) :: HNil )
                case ( ( None, lhs ), ( Some( _ ), rhs ) ) ⇒
                    ( Some( input ), lhs :: Operator.^ :: Coproduct[C]( rhs ) :: HNil )
                case ( ( _, lhs ), ( _, rhs ) ) ⇒
                    ( None, lhs :: Operator.^ :: Coproduct[C]( rhs ) :: HNil )
            }
        }
    }
}

trait NestedEvaluation1 {
    type Aux[I, O, L <: HList, O0 <: HList] = NestedEvaluation[I, O, L] { type Out0 = O0 }

    implicit def recursion[I, O, P, H <: HList, T <: HList](
        implicit
        neh: NestedEvaluation[I, O, H],
        net: NestedEvaluation[O, P, T]
    ) = new NestedEvaluation[I, P, H :: T] {
        type C = net.Out0 :+: T :+: CNil

        override type Out0 = neh.Out0 :: C :: HNil

        override def apply( input: I, tree: H :: T ) = tree match {
            case head :: tail ⇒ neh( input, head ) match {
                case ( Some( output ), lhs ) ⇒ net( output, tail ) match {
                    case ( output, rhs ) ⇒ ( output, lhs :: Coproduct[C]( rhs ) :: HNil )
                }
                case ( None, lhs ) ⇒ ( None, lhs :: Coproduct[C]( tail ) :: HNil )
            }
        }
    }
}