package com.eager.questioncloud.payment.domain

import com.eager.questioncloud.payment.dto.QuestionInfo
import com.eager.questioncloud.payment.scenario.PromotionScenario
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe

class QuestionOrderItemTest : BehaviorSpec() {
    init {
        Given("상품 프로모션 적용") {
            val questionId = 1L
            val questionOrderItem =
                QuestionOrderItem(questionInfo = QuestionInfo(questionId = questionId, creatorId = 1L, title = "문제", price = 10000))
            
            val promotionSalePrice = 5000
            val promotion = PromotionScenario.create(questionId, promotionSalePrice)
            When("상품에 프로모션을 적용하면") {
                questionOrderItem.applyPromotion(promotion)
                Then("상품 가격이 프로모션 가격으로 적용된다.") {
                    questionOrderItem.realPrice shouldBe promotionSalePrice
                    
                    questionOrderItem.appliedPromotion.shouldNotBeNull()
                    questionOrderItem.appliedPromotion?.title shouldBe promotion.title
                }
            }
        }
        
        Given("상픔 할인 적용") {
            val questionId = 1L
            val questionOrderItem =
                QuestionOrderItem(questionInfo = QuestionInfo(questionId = questionId, creatorId = 1L, title = "문제", price = 10000))
            
            val discountAmount = 1000
            
            When("상품에 할인을 적용하면") {
                questionOrderItem.applyDiscount(discountAmount)
                Then("상품 가격이 할인된다.") {
                    questionOrderItem.realPrice shouldBe questionOrderItem.originalPrice - discountAmount
                }
            }
        }
    }
}
