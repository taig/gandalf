package io.taig.bsts.android

import android.os.Build.VERSION_CODES.LOLLIPOP
import io.taig.bsts.{ Failure, Success }
import io.taig.bsts.android.reports._
import io.taig.bsts.android.syntax.feedback._
import io.taig.bsts.report.syntax.all._
import io.taig.bsts.rules.all._
import org.robolectric.annotation.Config

@Config( sdk = Array( LOLLIPOP ) )
class ReportTest extends Suite {
    it should "support required" in {
        //        rule.required.validate( "" ) match {
        //            case Success( _ )     ⇒ fail()
        //            case f @ Failure( _ ) ⇒ f.report shouldBe "Required"
        //        }

    }
}