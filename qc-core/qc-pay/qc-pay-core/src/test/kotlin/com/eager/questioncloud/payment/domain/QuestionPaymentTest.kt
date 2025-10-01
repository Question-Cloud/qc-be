package com.eager.questioncloud.payment.domain

import com.eager.questioncloud.payment.scenario.QuestionOrderScenario
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class QuestionPaymentTest : BehaviorSpec() {
    init {
        Given("결제 할인 적용") {
            val userId = 1L
            
            val expectedDiscountAmount = 5000
            
            val questionOrder = QuestionOrderScenario.create(5)
            val questioPayment = QuestionPayment.create(userId, questionOrder)
            val originalAmount = questioPayment.originalAmount
            
            When("결제에 할인을 적용하면") {
                questioPayment.applyDiscount(expectedDiscountAmount)
                Then("최종 결제 금액이 할인된다.") {
                    questioPayment.realAmount shouldBe originalAmount - expectedDiscountAmount
                }
            }
        }
    }
}
