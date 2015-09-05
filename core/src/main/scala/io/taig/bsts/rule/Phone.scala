package io.taig.bsts.rule

import io.taig.bsts.{ Adjust, Definition, Rule }

trait Phone extends Rule {
    override type Value = String
}

object Phone extends Phone {
    val pattern = "^\\+?0{0,2}[1-9]\\d{3,}$".r.pattern

    implicit val `Definition[Phone]` = Definition[Phone]( pattern.matcher( _: String ).matches() )

    implicit def `Adjust[Phone]`[R <: Phone] = Adjust[R]( _.replaceAll( "[\\s\\.-]", "" ) )
}