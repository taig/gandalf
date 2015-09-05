package test.io.taig.android.bsts

import android.os.Build.VERSION_CODES.LOLLIPOP
import android.view.View
import android.widget.{ EditText, FrameLayout }
import io.taig.android.bsts._
import io.taig.android.bsts.default.show._
import io.taig.bsts._
import io.taig.bsts.rule._
import org.robolectric.annotation.Config

import scala.language.implicitConversions

@Config( sdk = Array( LOLLIPOP ) )
class Validatable extends Suite {
    "A View (without validation rules)" should "validate to true" in {
        new View( context ).validate() shouldBe true
    }

    "A ViewGroup (without validation rules)" should "validate to true if it has no children" in {
        new FrameLayout( context ).validate() shouldBe true
    }

    it should "validate to true if none of its children have validation rules" in {
        val parent = new FrameLayout( context )
        parent.addView( new View( context ) )
        parent.addView( new View( context ) )
        parent.addView( new View( context ) )

        parent.validate() shouldBe true
    }

    it should "fail if one of its children does not pass validation" in {
        val parent = new FrameLayout( context )

        val editText = new EditText( context )

        parent.addView( new View( context ) )
        parent.addView( editText )
        parent.addView( new View( context ) )

        new ValidatableRule( Required[String]() )
        editText obeys Required[String]()

        editText.validate() shouldBe false
        parent.validate() shouldBe false
    }

    it should "succeed if all its children pass validation" in {
        val parent = new FrameLayout( context )

        val editText1 = new EditText( context )
        editText1.setText( "a" )

        val editText2 = new EditText( context )
        editText2.setText( "b" )

        parent.addView( editText1 )
        parent.addView( new View( context ) )
        parent.addView( editText2 )

        editText1 obeys Required[String]()
        editText2 obeys Required[String]()

        editText1.validate() shouldBe true
        editText2.validate() shouldBe true
        parent.validate() shouldBe true
    }

    it should "work with deeply nested validation children" in {
        val parentParent = new FrameLayout( context )
        val parent = new FrameLayout( context )

        val editText1 = new EditText( context )
        editText1.setText( "a" )

        val editText2 = new EditText( context )
        editText2.setText( "b" )

        parentParent.addView( parent )
        parentParent.addView( new FrameLayout( context ) )
        parent.addView( new FrameLayout( context ) )
        parent.addView( editText1 )
        parent.addView( new View( context ) )
        parent.addView( editText2 )

        editText1 obeys Required[String]()
        editText2 obeys Required[String]()

        editText1.validate() shouldBe true
        editText2.validate() shouldBe true
        parentParent.validate() shouldBe true

        editText1.setText( "" )
        editText1.validate() shouldBe false
        parentParent.validate() shouldBe false
    }

    it should "be possible to clear all error messages" in {
        val parent = new FrameLayout( context )

        val editText1 = new EditText( context )
        val editText2 = new EditText( context )

        parent.addView( editText1 )
        parent.addView( new View( context ) )
        parent.addView( editText2 )

        editText1 obeys Required[String]()
        editText2 obeys Required[String]()

        parent.validate()

        editText1.indication shouldBe Some( Seq( context.getString( R.string.bsts_required ) ) )
        editText2.indication shouldBe Some( Seq( context.getString( R.string.bsts_required ) ) )

        parent.clear()

        editText1.indication shouldBe None
        editText2.indication shouldBe None
    }

    it should "be possible to check the validation state without affecting the UI" in {
        val parent = new FrameLayout( context )

        val editText1 = new EditText( context )
        val editText2 = new EditText( context )

        parent.addView( editText1 )
        parent.addView( new View( context ) )
        parent.addView( editText2 )

        editText1 obeys Required[String]()
        editText2 obeys Required[String]()

        parent.check() shouldBe false

        editText1.indication shouldBe None
        editText2.indication shouldBe None

        parent.validate()

        editText1.indication shouldBe Some( Seq( context.getString( R.string.bsts_required ) ) )
        editText2.indication shouldBe Some( Seq( context.getString( R.string.bsts_required ) ) )

        parent.clear()
        parent.check() shouldBe false

        editText1.indication shouldBe None
        editText2.indication shouldBe None
    }
}