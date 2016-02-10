package io.taig.bsts.ops

import io.taig.bsts._

import scala.annotation.StaticAnnotation
import scala.reflect.macros.whitebox

package object dsl {
    class argument[O <: Operator.Binary]( operator: O ) extends StaticAnnotation

    def impl[A: c.WeakTypeTag, I: c.WeakTypeTag, O: c.WeakTypeTag, P: c.WeakTypeTag]( c: whitebox.Context )(
        b: c.Expr[Validation[I, O]]
    ): c.Expr[Validation[I, P]] = {
        import c.universe._

        val a = c.Expr[A]( c.prefix.tree.children.last )
        val o = c.Expr[Operator.Binary]( c.macroApplication.symbol.annotations.last.tree.children.last )

        val expr = if ( o.actualType =:= weakTypeOf[Operator.~>.type] ) {
            ( a, b ) match {
                // transformation ~> rule
                case _ if a.actualType <:< weakTypeOf[Transformation[_, I, O, _]]
                    && b.actualType <:< weakTypeOf[Rule[_, I, _]] ⇒
                    q"""
                    new Policy.Transformation(
                        ( $a :: HNil ) :: $o :: ( $b :: HNil ) :: HNil
                    )"""
                // rule ~> transformation
                case _ if a.actualType <:< weakTypeOf[Rule[_, I, _]] &&
                    b.actualType <:< weakTypeOf[Transformation[_, I, O, _]] ⇒
                    q"""
                    new Policy.Transformation(
                        ( $a :: HNil ) :: $o :: ( $b :: HNil ) :: HNil
                    )"""
                // transformation ~> transformation
                case _ if a.actualType <:< weakTypeOf[Transformation[_, I, O, _]] &&
                    b.actualType <:< weakTypeOf[Transformation[_, O, P, _]] ⇒
                    q"""
                    new Policy.Transformation(
                        ( $a :: HNil ) :: $o :: ( $b :: HNil ) :: HNil
                    )"""
                // ( transformation ~> transformation ) ~> transformation
                case _ if a.actualType <:< weakTypeOf[Policy.Transformation[I, O, _, _]] &&
                    b.actualType <:< weakTypeOf[Transformation[_, O, P, _]] ⇒
                    q"""
                    new Policy.Transformation(
                        $a.validations :: $o :: ( $b :: HNil ) :: HNil
                    )"""
                // transformation ~> ( transformation ~> transformation )
                case _ if a.actualType <:< weakTypeOf[Transformation[_, I, O, _]] &&
                    b.actualType <:< weakTypeOf[Policy.Transformation[O, P, _, _]] ⇒
                    q"""
                    new Policy.Transformation(
                        ( $a :: HNil ) :: $o :: $b.validations :: HNil
                    )"""
                // ( transformation ~> transformation ) ~> ( transformation ~> transformation )
                case _ if a.actualType <:< weakTypeOf[Policy.Transformation[I, O, _, _]] &&
                    b.actualType <:< weakTypeOf[Policy.Transformation[O, P, _, _]] ⇒
                    q"""
                    new Policy.Transformation(
                        $a.validations :: $o :: $b.validations :: HNil
                    )"""
                // ( transformation ~> transformation ) ~> rule
                case _ if a.actualType <:< weakTypeOf[Policy.Transformation[I, O, _, _]] &&
                    b.actualType <:< weakTypeOf[Rule[_, O, _]] ⇒
                    q"""
                    import shapeless.ops.hlist._
    
                    val p1 = Prepend[${weakTypeOf[A].typeArgs.last}, ${o.actualType} :: HNil]
                    val p2 = Prepend[p1.Out, ( ${b.actualType} :: HNil ) :: HNil]
    
                    new Policy.Transformation[${weakTypeOf[I]}, ${weakTypeOf[P]}, p2.Out](
                        p2( p1( $a.validations, $o :: HNil ), ( $b :: HNil ) :: HNil )
                    )"""
                // transformation ~> ( rule && rule )
                case _ if a.actualType <:< weakTypeOf[Transformation[_, I, O, _]] &&
                    b.actualType <:< weakTypeOf[Policy.Rule[O, _, _]] ⇒
                    q"""
                    new Policy.Transformation(
                        ( $a :: HNil ) :: $o :: ( $b.validations :: HNil )
                    )"""
                // ( transformation ~> transformation ) ~> ( rule && rule )
                case _ if a.actualType <:< weakTypeOf[Policy.Transformation[I, O, _, _]] &&
                    b.actualType <:< weakTypeOf[Policy.Rule[O, _, _]] ⇒
                    q"""
                    import shapeless.ops.hlist._
    
                    val p1 = Prepend[${weakTypeOf[A].typeArgs.last}, ${o.actualType} :: HNil]
                    val p2 = Prepend[p1.Out, ${b.actualType.typeArgs.last} :: HNil]
    
                    new Policy.Transformation[${weakTypeOf[I]}, ${weakTypeOf[P]}, p2.Out](
                        p2( p1( $a.validations, $o :: HNil ), $b.validations :: HNil )
                    )"""
            }
        } else {
            ( a, b ) match {
                // rule && rule
                case _ if a.actualType <:< weakTypeOf[Rule[_, I, _]] && b.actualType <:< weakTypeOf[Rule[_, I, _]] ⇒
                    q"""
                    new Policy.Rule(
                        ( $a :: HNil ) :: $o :: ( $b :: HNil ) :: HNil
                    )"""
                // ( rule && rule ) && rule
                case _ if a.actualType <:< weakTypeOf[Policy.Rule[I, _, _]] &&
                    b.actualType <:< weakTypeOf[Rule[_, I, _]] ⇒
                    q"""
                    new Policy.Rule(
                        $a.validations :: $o :: ( $b :: HNil ) :: HNil
                    )"""
                // rule && ( rule && rule )
                case _ if a.actualType <:< weakTypeOf[Rule[_, I, _]] &&
                    b.actualType <:< weakTypeOf[Policy.Rule[I, _, _]] ⇒
                    q"""
                    new Policy.Rule(
                        ( $a :: HNil ) :: $o :: $b.validations :: HNil
                    )"""
                // ( rule && rule ) && ( rule && rule )
                case _ if a.actualType <:< weakTypeOf[Policy.Rule[I, _, _]] &&
                    b.actualType <:< weakTypeOf[Policy.Rule[I, _, _]] ⇒
                    q"""
                    new Policy.Rule(
                        $a.validations :: $o :: $b.validations :: HNil
                    )"""
            }
        }

        c.Expr[Validation[I, P]] {
            q"""
            import io.taig.bsts._
            import shapeless._
            $expr
            """
        }
    }
}