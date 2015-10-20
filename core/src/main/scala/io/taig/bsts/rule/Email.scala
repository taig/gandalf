package io.taig.bsts.rule

import io.taig.bsts.{ Definition, Rule }

trait Email extends Rule {
    override type Value = String
}

object Email extends Email {
    val pattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$".r.pattern

    implicit val `Definition[Email]` = Definition[Email]( pattern.matcher( _: String ).matches() )
}