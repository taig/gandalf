package io.taig.bsts.android

import android.support.design.widget.TextInputLayout
import android.view.View
import android.widget.AdapterView.INVALID_POSITION
import android.widget.{ AdapterView, CompoundButton, RadioGroup, TextView }

trait Injection[V <: View, -T] {
    def inject( view: V, value: T ): Unit

    def contramapL[W <: View]( f: W ⇒ V ): Injection[W, T] = Injection.instance { ( view, value ) ⇒
        inject( f( view ), value )
    }

    def contramapR[U]( f: U ⇒ T ): Injection[V, U] = Injection.instance { ( view, value ) ⇒
        inject( view, f( value ) )
    }
}

object Injection {
    def apply[V <: View, T]( implicit i: Injection[V, T] ): Injection[V, T] = i

    def instance[V <: View, T]( f: ( V, T ) ⇒ Unit ): Injection[V, T] = new Injection[V, T] {
        override def inject( view: V, value: T ): Unit = f( view, value )
    }

    implicit val `Injection[AdapterView, Int]`: Injection[AdapterView[_], Int] = instance( _.setSelection( _ ) )

    implicit val `Injection[AdapterView, Option[Int]]`: Injection[AdapterView[_], Option[Int]] = {
        Injection[AdapterView[_], Int].contramapR( _.getOrElse( INVALID_POSITION ) )
    }

    implicit val `Injection[CompoundButton, Int]`: Injection[CompoundButton, Boolean] = instance( _.setChecked( _ ) )

    implicit val `Injection[RadioGroup, Int]`: Injection[RadioGroup, Int] = instance( _.check( _ ) )

    implicit val `Injection[RadioGroup, Option[Int]]`: Injection[RadioGroup, Option[Int]] = instance {
        case ( view, Some( value ) ) ⇒ view.check( value )
        case ( view, None )          ⇒ view.clearCheck()
    }

    implicit val `Injection[TextView, CharSequence]`: Injection[TextView, CharSequence] = instance( _.setText( _ ) )

    implicit val `Injection[TextView, Option[CharSequence]]`: Injection[TextView, Option[CharSequence]] = {
        Injection[TextView, CharSequence].contramapR( _.orNull )
    }

    implicit val `Injection[TextInputLayout, CharSequence]`: Injection[TextInputLayout, CharSequence] = {
        Injection[TextView, CharSequence].contramapL( _.getEditText )
    }

    implicit val `Injection[TextInputLayout, Option[CharSequence]]`: Injection[TextInputLayout, Option[CharSequence]] = {
        Injection[TextView, Option[CharSequence]].contramapL( _.getEditText )
    }
}