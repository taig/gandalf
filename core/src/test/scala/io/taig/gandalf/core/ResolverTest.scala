package io.taig.gandalf.core

import io.taig.gandalf.core
import io.taig.gandalf.core._
import shapeless.test.illTyped

class ResolverTest extends Suite {
    "Condition" should "have a String representation" in {
        Resolver[condition.success, Rule.Condition].toString shouldBe
            "Resolver(success ~> Condition)"
    }

    it should "have a an equals implementation" in {
        Resolver[condition.success, Rule.Condition] == Resolver[condition.success, Rule.Condition] shouldBe
            true
        Resolver[condition.success, Rule.Condition] == Resolver[condition.failure, Rule.Condition] shouldBe
            false
        Resolver[condition.success, Rule.Condition] == 0 shouldBe
            false
    }

    it should "resolve to Condition" in {
        type A = condition.success
        implicitly[Resolver[A]] shouldBe Resolver.instance[A, Rule.Condition]
    }

    "Mutation" should "have a String representation" in {
        Resolver[mutation.success, Rule.Mutation].toString shouldBe
            "Resolver(success ~> Mutation)"
    }

    it should "resolve to Mutation" in {
        type A = mutation.success
        implicitly[Resolver[A]] shouldBe Resolver.instance[A, Rule.Mutation]
    }

    "Transition" should "have a String representation" in {
        Resolver[transition.string, Rule.Transition].toString shouldBe
            "Resolver(string ~> Transition)"
    }

    it should "resolve a Transition to Transition" in {
        type A = transition.string
        implicitly[Resolver[A]] shouldBe Resolver.instance[A, Rule.Transition]
    }

    "&&" should "resolve Conditions to Condition" in {
        type A = condition.success && condition.success
        implicitly[Resolver[A]] shouldBe Resolver.instance[A, Rule.Condition]
    }

    it should "resolve Mutations to Mutation" in {
        type A = mutation.success && mutation.success
        implicitly[Resolver[A]] shouldBe Resolver.instance[A, Rule.Mutation]
    }

    it should "resolve Transitions to Transition" in {
        type A = transition.string && transition.string
        implicitly[Resolver[A]] shouldBe Resolver.instance[A, Rule.Transition]
    }

    it should "resolve Mutation with Condition to Mutation" in {
        type A = mutation.success && condition.success
        implicitly[Resolver[A]] shouldBe Resolver.instance[A, Rule.Mutation]

        type B = condition.success && mutation.success
        implicitly[Resolver[B]] shouldBe Resolver.instance[B, Rule.Mutation]
    }

    it should "resolve Transition with Condition to Mutation" in {
        type A = transition.string && condition.success
        implicitly[Resolver[A]] shouldBe Resolver.instance[A, Rule.Mutation]

        type B = condition.success && transition.string
        implicitly[Resolver[B]] shouldBe Resolver.instance[B, Rule.Mutation]
    }

    "||" should "resolve Conditions to Condition" in {
        type A = condition.success || condition.success
        implicitly[Resolver[A]] shouldBe Resolver.instance[A, Rule.Condition]
    }

    it should "resolve Mutations to Mutation" in {
        type A = mutation.success || mutation.success
        implicitly[Resolver[A]] shouldBe Resolver.instance[A, Rule.Mutation]
    }

    it should "not resolve Transitions" in {
        illTyped( "implicitly[Resolver[transition.string || transition.string]]" )
    }

    it should "resolve Mutation with Condition to Mutation" in {
        type A = mutation.success || condition.success
        implicitly[Resolver[A]] shouldBe Resolver.instance[A, Rule.Mutation]

        type B = condition.success || mutation.success
        implicitly[Resolver[B]] shouldBe Resolver.instance[B, Rule.Mutation]
    }

    it should "not resolve Transition with Condition" in {
        illTyped( "implicitly[Resolver[transition.string || condition.success]]" )
        illTyped( "implicitly[Resolver[condition.success || transition.string]]" )
    }

    "not" should "have a String representation" in {
        Resolver[not[condition.success], Rule.Condition].toString shouldBe
            "Resolver(not(success) ~> Condition)"
    }

    it should "resolve a Condition to Condition" in {
        type A = not[condition.success]
        implicitly[Resolver[A]] shouldBe Resolver.instance[A, Rule.Condition]
    }

    it should "not resolve a Mutation" in {
        illTyped( "implicitly[Resolver[not[mutation.success]]]" )
    }

    it should "not resolve a Transition" in {
        illTyped( "implicitly[Resolver[not[transition.string]]]" )
    }

    it should "resolve nested Rules" in {
        type A = not[not[condition.success]]
        implicitly[Resolver[A]] shouldBe Resolver.instance[A, Rule.Condition]

        type B = not[not[A]]
        implicitly[Resolver[B]] shouldBe Resolver.instance[B, Rule.Condition]
    }

    "not[&&]" should "resolve Conditions to Condition" in {
        type A = not[condition.success && condition.success]
        implicitly[Resolver[A]] shouldBe Resolver.instance[A, Rule.Condition]
    }

    it should "not resolve Mutations" in {
        illTyped( "implicitly[Resolver[not[mutation.success && mutation.success]]]" )
    }

    it should "not resolve Transitions" in {
        illTyped( "implicitly[Resolver[not[transition.string && transition.string]]]" )
    }

    it should "resolve Mutation with Condition to Mutation" in {
        type A = not[mutation.success && condition.success]
        implicitly[Resolver[A]] shouldBe Resolver.instance[A, Rule.Mutation]

        type B = not[condition.success && mutation.success]
        implicitly[Resolver[B]] shouldBe Resolver.instance[B, Rule.Mutation]
    }

    it should "resolve Transition with Condition to Mutation" in {
        type A = not[transition.string && condition.success]
        implicitly[Resolver[A]] shouldBe Resolver.instance[A, Rule.Mutation]

        type B = not[condition.success && transition.string]
        implicitly[Resolver[B]] shouldBe Resolver.instance[B, Rule.Mutation]
    }

    it should "resolve compositions" in {
        type A = not[composition.and.success]
        implicitly[Resolver[A]] shouldBe Resolver.instance[A, Rule.Condition]

        type B = not[composition.and.failure]
        implicitly[Resolver[B]] shouldBe Resolver.instance[B, Rule.Condition]
    }

    "not[||]" should "resolve Conditions to Condition" in {
        type A = not[condition.success || condition.success]
        implicitly[Resolver[A]] shouldBe Resolver.instance[A, Rule.Condition]
    }

    it should "not resolve Mutations" in {
        illTyped( "implicitly[Resolver[not[mutation.success || mutation.success]]]" )
    }

    it should "not resolve Transitions" in {
        illTyped( "implicitly[Resolver[not[transition.string || transition.string]]]" )
    }

    it should "resolve Mutation with Condition to Mutation" in {
        type A = not[mutation.success || condition.success]
        implicitly[Resolver[A]] shouldBe Resolver.instance[A, Rule.Mutation]

        type B = not[condition.success || mutation.success]
        implicitly[Resolver[B]] shouldBe Resolver.instance[B, Rule.Mutation]
    }

    it should "not resolve Transition with Condition" in {
        illTyped( "implicitly[Resolver[not[transition.string || condition.success]]]" )
        illTyped( "implicitly[Resolver[not[condition.success || transition.string]]]" )
    }

    it should "resolve compositions" in {
        type A = not[composition.or.success]
        implicitly[Resolver[A]] shouldBe Resolver.instance[A, Rule.Condition]

        type B = not[composition.or.failure]
        implicitly[Resolver[B]] shouldBe Resolver.instance[B, Rule.Condition]
    }
}