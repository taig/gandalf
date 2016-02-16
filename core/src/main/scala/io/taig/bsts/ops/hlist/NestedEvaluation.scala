package io.taig.bsts.ops.hlist

import cats.data.Xor
import cats.data.Xor._
import io.taig.bsts._
import io.taig.bsts.ops.dsl.Operator
import io.taig.bsts.ops.{ Computed, Unevaluated }
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

    implicit def term[N <: String, I, O, A <: HList] = {
        new NestedEvaluation[I, O, Term.Aux[N, I, O, A, Xor[Error[N, A], O]] :: HNil] {
            override type Out0 = Xor[Error[N, A], O] :: HNil

            override def apply( input: I, tree: Term.Aux[N, I, O, A, Xor[Error[N, A], O]] :: HNil ) = tree match {
                case term :: HNil ⇒ term.validate( input ) match {
                    case v @ Right( output ) ⇒ ( Some( output ), Computed( v :: HNil ) )
                    case i @ Left( _ )       ⇒ ( None, Computed( i :: HNil ) )
                }
            }
        }
    }

    implicit def termNoError[N <: String, I, O, A <: HList] = {
        new NestedEvaluation[I, O, Term.Aux[N, I, O, A, O] :: HNil] {
            override type Out0 = Right[O] :: HNil

            override def apply( input: I, tree: Term.Aux[N, I, O, A, O] :: HNil ) = tree match {
                case term :: HNil ⇒
                    val output = term.validate( input )
                    ( Some( output ), Computed( Right( output ) :: HNil ) )
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
}

trait NestedEvaluation0 extends NestedEvaluation1 {
    implicit def operationR[T, L <: HList, O <: Operator.Binary, R <: HList](
        implicit
        nel: NestedEvaluation[T, T, L],
        ner: NestedEvaluation[T, T, R],
        p:   Printer[R],
        ev:  O <:!< Operator.~>.type
    ) = new NestedEvaluation[T, T, L :: O :: R] {
        type C = Computed[ner.Out0] :+: Unevaluated[R] :+: CNil

        override type Out0 = Computed[nel.Out0] :: O :: C :: HNil

        override def apply( input: T, tree: L :: O :: R ) = tree match {
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

trait NestedEvaluation1 {
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