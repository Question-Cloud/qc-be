package com.eager.questioncloud.payment.domain

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.payment.scenario.CouponScenario
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class CouponPolicyTest : BehaviorSpec() {
    init {
        Given("금액 할인 쿠폰의 경우") {
            val userId = 1L
            val questionId = 1L
            val expectedDiscountAmount = 1000
            
            val coupon = CouponScenario.productFixedCoupon(questionId = questionId, discount = expectedDiscountAmount)
            val userCoupon = UserCoupon.create(userId, coupon)
            
            val originalAmount = 10000
            
            val couponPolicy = Coupon(coupon, userCoupon)
            When("할인 금액 계산을 하면") {
                val discountAmount = couponPolicy.getDiscountAmount(originalAmount)
                Then("정해진 고정 금액이 반환된다.") {
                    discountAmount shouldBe expectedDiscountAmount
                }
            }
        }
        
        Given("퍼센트 할인 쿠폰의 경우") {
            val userId = 1L
            val questionId = 1L
            val originalAmount = 10000
            val discountPercent = 10
            
            val coupon = CouponScenario.productPercentCoupon(questionId = questionId, percent = discountPercent)
            val userCoupon = UserCoupon.create(userId, coupon)
            
            val expectedDiscountAmount = originalAmount * discountPercent / 100
            
            val couponPolicy = Coupon(coupon, userCoupon)
            When("할인 금액 계산을 하면") {
                val discountAmount = couponPolicy.getDiscountAmount(originalAmount)
                Then("정해진 고정 금액이 반환된다.") {
                    discountAmount shouldBe expectedDiscountAmount
                }
            }
        }
        
        Given("최대 할인 금액이 정해져있는 퍼센트 할인 쿠폰의 경우") {
            val userId = 1L
            val questionId = 1L
            val discountPercent = 40
            val originalAmount = 100000
            val maximumDiscountAmount = 1000
            
            val coupon =
                CouponScenario.productPercentCoupon(questionId = questionId, percent = discountPercent, maxDiscount = maximumDiscountAmount)
            val userCoupon = UserCoupon.create(userId, coupon)
            
            val couponPolicy = Coupon(coupon, userCoupon)
            When("할인 금액이 최대 할인 금액을 초과하게 되면") {
                val discountAmount = couponPolicy.getDiscountAmount(originalAmount)
                Then("최대할인 금액으로 계산된다.") {
                    discountAmount shouldBe maximumDiscountAmount
                }
            }
        }
        
        Given("최소 금액이 정해져있는 할인 쿠폰의 경우") {
            val userId = 1L
            val questionId = 1L
            val discountPercent = 40
            val originalAmount = 100000
            
            val coupon =
                CouponScenario.productPercentCoupon(questionId = questionId, percent = discountPercent, minPurchase = originalAmount + 1000)
            val userCoupon = UserCoupon.create(userId, coupon)
            
            val couponPolicy = Coupon(coupon, userCoupon)
            When("최소 주문 금액을 만족한 채 쿠폰 적용을 시도하면") {
                Then("예외가 발생한다.") {
                    val exception = shouldThrow<CoreException> {
                        couponPolicy.getDiscountAmount(originalAmount)
                    }
                    exception.error shouldBe Error.WRONG_COUPON
                }
            }
        }
    }
}
