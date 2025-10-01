package com.eager.questioncloud.payment.domain

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.payment.scenario.CouponScenario
import com.eager.questioncloud.payment.scenario.QuestionOrderScenario
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class QuestionPaymentTest : BehaviorSpec() {
    init {
        Given("결제 할인 쿠폰 적용") {
            val userId = 1L
            
            val expectedDiscountAmount = 5000
            val paymentCoupon = CouponScenario.paymentFixedCoupon(discount = expectedDiscountAmount)
            val userCoupon = UserCoupon.create(userId, paymentCoupon)
            val coupon = Coupon(paymentCoupon, userCoupon)
            
            val questionOrder = QuestionOrderScenario.create(5)
            val questioPayment = QuestionPayment.create(userId, questionOrder)
            When("결제 할인 쿠폰을 적용하면") {
                questioPayment.applyPaymentCoupon(coupon)
                Then("최종 결제 금액이 할인된다.") {
                    questioPayment.realAmount shouldBe questioPayment.originalAmount - expectedDiscountAmount
                    
                    questioPayment.paymentDiscount.size shouldBe 1
                    questioPayment.paymentDiscount[0].discountAmount shouldBe expectedDiscountAmount
                    questioPayment.paymentDiscount[0].name shouldBe paymentCoupon.title
                }
            }
        }
        
        Given("결제 할인 쿠폰이 아닌 쿠폰을 사용하려고 할 때") {
            val userId = 1L
            
            val expectedDiscountAmount = 5000
            val notPaymentCoupon = CouponScenario.productFixedCoupon(questionId = 1L, discount = expectedDiscountAmount)
            val userCoupon = UserCoupon.create(userId, notPaymentCoupon)
            val coupon = Coupon(notPaymentCoupon, userCoupon)
            
            val questionOrder = QuestionOrderScenario.create(5)
            val questioPayment = QuestionPayment.create(userId, questionOrder)
            When("결제 할인 쿠폰을 적용하면") {
                Then("예외가 발생한다.") {
                    val exception = shouldThrow<CoreException> {
                        questioPayment.applyPaymentCoupon(coupon)
                    }
                    exception.error shouldBe Error.WRONG_COUPON
                }
            }
        }
    }
}
