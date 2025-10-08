package com.eager.questioncloud.payment.domain

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.payment.scenario.CouponScenario
import com.eager.questioncloud.payment.scenario.QuestionOrderScenario
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class PaymentCouponTest : BehaviorSpec() {
    init {
        Given("고정 금액 할인 결제 쿠폰이 있을 때") {
            val userId = 1L
            val order = QuestionOrderScenario.create(1)
            
            val expectedDiscountAmount = 1000
            
            val couponInformation = CouponScenario.paymentFixedCoupon(discount = expectedDiscountAmount)
            val userCoupon = UserCoupon.create(userId, couponInformation)
            
            val paymentCoupon = Coupon.createPaymentCoupon(couponInformation, userCoupon)
            val questionPayment = QuestionPayment.create(userId, order)
            When("결제에 쿠폰을 적용하면") {
                paymentCoupon.apply(questionPayment)
                Then("고정 금액만큼 할인이 적용된다.") {
                    questionPayment.realAmount shouldBe questionPayment.originalAmount - expectedDiscountAmount
                    questionPayment.appliedPaymentCoupons.size shouldBe 1
                    questionPayment.appliedPaymentCoupons[0].couponInformation.title shouldBe couponInformation.title
                    questionPayment.appliedPaymentCoupons[0].appliedDiscountAmount shouldBe expectedDiscountAmount
                }
            }
        }
        
        Given("퍼센트 할인 결제 쿠폰이 있을 때") {
            val userId = 1L
            val order = QuestionOrderScenario.create(1)
            val questionPayment = QuestionPayment.create(userId, order)
            
            val discountPercent = 10
            val expectedDiscountAmount = questionPayment.originalAmount * discountPercent / 100
            
            val couponInformation = CouponScenario.paymentPercentCoupon(percent = discountPercent)
            val userCoupon = UserCoupon.create(userId, couponInformation)
            
            val productCoupon = Coupon.createPaymentCoupon(couponInformation, userCoupon)
            
            When("결제에 쿠폰을 적용하면") {
                productCoupon.apply(questionPayment)
                Then("정해진 퍼센트 비율만큼 할인이 적용된다.") {
                    questionPayment.realAmount shouldBe questionPayment.originalAmount - expectedDiscountAmount
                    questionPayment.appliedPaymentCoupons.size shouldBe 1
                    questionPayment.appliedPaymentCoupons[0].couponInformation.title shouldBe couponInformation.title
                    questionPayment.appliedPaymentCoupons[0].appliedDiscountAmount shouldBe expectedDiscountAmount
                }
            }
        }
        
        Given("최대 할인 금액이 정해져있는 퍼센트 할인 쿠폰의 경우") {
            val userId = 1L
            val order = QuestionOrderScenario.create(1)
            val questionPayment = QuestionPayment.create(userId, order)
            
            val discountPercent = 10
            val maximumDiscountAmount = 100
            
            val couponInformation = CouponScenario.paymentPercentCoupon(percent = discountPercent, maxDiscount = maximumDiscountAmount)
            val userCoupon = UserCoupon.create(userId, couponInformation)
            
            val productCoupon = Coupon.createPaymentCoupon(couponInformation, userCoupon)
            When("상품에 쿠폰을 적용하면") {
                productCoupon.apply(questionPayment)
                Then("최대 할인 금액까지만 할인이 적용된다.") {
                    questionPayment.realAmount shouldBe questionPayment.originalAmount - maximumDiscountAmount
                    questionPayment.appliedPaymentCoupons.size shouldBe 1
                    questionPayment.appliedPaymentCoupons[0].couponInformation.title shouldBe couponInformation.title
                    questionPayment.appliedPaymentCoupons[0].appliedDiscountAmount shouldBe maximumDiscountAmount
                }
            }
        }
        
        Given("최소 금액이 정해져있는 할인 쿠폰의 경우") {
            val userId = 1L
            val order = QuestionOrderScenario.create(1)
            val questionPayment = QuestionPayment.create(userId, order)
            
            val discountPercent = 10
            
            val couponInformation =
                CouponScenario.paymentPercentCoupon(percent = discountPercent, minPurchase = questionPayment.originalAmount + 10000)
            val userCoupon = UserCoupon.create(userId, couponInformation)
            
            val productCoupon = Coupon.createPaymentCoupon(couponInformation, userCoupon)
            
            When("최소 주문 금액을 만족하지 않은 채 쿠폰 적용을 시도하면") {
                Then("예외가 발생한다.") {
                    val exception = shouldThrow<CoreException> {
                        productCoupon.apply(questionPayment)
                    }
                    exception.error shouldBe Error.WRONG_COUPON
                }
            }
        }
    }
}
