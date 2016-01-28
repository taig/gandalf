package io.taig.bsts.android

import android.os.Build.VERSION_CODES.LOLLIPOP
import android.widget._
import io.taig.bsts.android.syntax.feedback._
import org.robolectric.annotation.Config

@Config( sdk = Array( LOLLIPOP ) )
class FeedbackTest extends Suite {
    it should "support TextView" in {
        val view = new TextView( context )
        view.feedback = "foo"
        view.getError shouldBe "foo"

        view.feedback match {
            case Some( error ) ⇒ error shouldBe view.getError
            case None          ⇒ fail()
        }

        view.clear
        view.getError shouldBe null
    }

    it should "support EditText" in {
        val view = new EditText( context )
        view.feedback = "foo"
        view.getError shouldBe "foo"

        view.feedback match {
            case Some( error ) ⇒ error shouldBe view.getError
            case None          ⇒ fail()
        }

        view.clear
        view.getError shouldBe null
    }
}