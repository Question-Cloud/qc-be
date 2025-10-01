package com.eager.questioncloud.payment.domain

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.payment.dto.QuestionInfo
import com.eager.questioncloud.payment.scenario.CouponScenario
import com.eager.questioncloud.payment.scenario.PromotionScenario
import io.kotest.assertions.throwables.shouldThrow
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
        
        Given("상픔 쿠폰 적용") {
            val userId = 1L
            val questionId = 1L
            val questionOrderItem =
                QuestionOrderItem(questionInfo = QuestionInfo(questionId = questionId, creatorId = 1L, title = "문제", price = 10000))
            
            val discountAmount = 1000
            val coupon = CouponScenario.productFixedCoupon(questionId, discountAmount)
            val userCoupon = UserCoupon.create(userId, coupon)
            val couponPolicy = Coupon(coupon, userCoupon)
            
            When("상품에 쿠폰을 적용하면") {
                questionOrderItem.applyDiscount(couponPolicy)
                Then("상품 가격이 할인된다.") {
                    questionOrderItem.realPrice shouldBe questionOrderItem.originalPrice - discountAmount
                    
                    questionOrderItem.orderDiscountHistories.size shouldBe 1
                    questionOrderItem.orderDiscountHistories[0].discountAmount shouldBe discountAmount
                    questionOrderItem.orderDiscountHistories[0].name shouldBe coupon.title
                }
            }
        }
        
        Given("쿠폰 대상이 아닌 상품에 쿠폰을 적용할 때") {
            val userId = 1L
            val questionId = 1L
            val couponTargetQuestionId = 2L
            val questionOrderItem =
                QuestionOrderItem(questionInfo = QuestionInfo(questionId = questionId, creatorId = 1L, title = "문제", price = 10000))
            
            val discountAmount = 1000
            val coupon = CouponScenario.productFixedCoupon(couponTargetQuestionId, discountAmount)
            val userCoupon = UserCoupon.create(userId, coupon)
            val couponPolicy = Coupon(coupon, userCoupon)
            
            When("쿠폰 적용을 시도하면") {
                Then("예외가 발생한다.") {
                    val exception = shouldThrow<CoreException> {
                        questionOrderItem.applyDiscount(couponPolicy)
                    }
                    exception.error shouldBe Error.WRONG_COUPON
                }
            }
        }
        
        Given("상품 쿠폰이 아닌 쿠폰을 상품에 적용할 때") {
            val userId = 1L
            val questionId = 1L
            val questionOrderItem =
                QuestionOrderItem(questionInfo = QuestionInfo(questionId = questionId, creatorId = 1L, title = "문제", price = 10000))
            
            val discountAmount = 1000
            val notProductCoupon = CouponScenario.paymentFixedCoupon(discountAmount)
            val userCoupon = UserCoupon.create(userId, notProductCoupon)
            val coupon = Coupon(notProductCoupon, userCoupon)
            
            When("쿠폰 적용을 시도하면") {
                Then("예외가 발생한다.") {
                    val exception = shouldThrow<CoreException> {
                        questionOrderItem.applyDiscount(coupon)
                    }
                    exception.error shouldBe Error.WRONG_COUPON
                }
            }
        }
    }
}
