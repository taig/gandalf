package io.taig.bsts.syntax

import io.taig.bsts._
import io.taig.bsts.ops
import shapeless._

import scala.language.implicitConversions

trait dsl {
    implicit def policyRuleSyntax[T, V <: HList, R <: HList](
        policy: Policy.Rule[T, V, R]
    ): ops.dsl.rule[T, Policy.Rule[T, V, R]] = new ops.dsl.rule( policy )

    implicit def policyRuleTransformationSyntax[T, V <: HList, R <: HList](
        policy: Policy.Rule[T, V, R]
    ): ops.dsl.transformation[T, T, Policy.Rule[T, V, R]] = new ops.dsl.transformation( policy )

    implicit def ruleSyntax[N <: String, T, A <: HList](
        rule: Rule[N, T, A]
    ): ops.dsl.rule[T, Rule[N, T, A]] = new ops.dsl.rule( rule )

    implicit def ruleTransformationSyntax[N <: String, T, A <: HList](
        rule: Rule[N, T, A]
    ): ops.dsl.transformation[T, T, Rule[N, T, A]] = new ops.dsl.transformation( rule )

    implicit def policyTransformationSyntax[I, O, V <: HList, R <: HList](
        policy: Policy.Transformation[I, O, V, R]
    ): ops.dsl.transformation[I, O, Policy.Transformation[I, O, V, R]] = new ops.dsl.transformation( policy )

    implicit def transformationSyntax[N <: String, I, O, A <: HList](
        transformation: Transformation[N, I, O, A]
    ): ops.dsl.transformation[I, O, Transformation[N, I, O, A]] = new ops.dsl.transformation( transformation )
}

object dsl extends dsl