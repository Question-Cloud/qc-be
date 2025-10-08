package com.eager.questioncloud.payment.domain

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.payment.scenario.CouponScenario
import com.eager.questioncloud.payment.scenario.QuestionOrderScenario
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class ProductCouponTest : BehaviorSpec() {
    init {
        Given("금액 할인 쿠폰이 있을 때") {
            val userId = 1L
            val order = QuestionOrderScenario.create(1)
            val questionId = order.items[0].questionInfo.questionId
            val originalAmount = order.items[0].priceAfterPromotion
            
            val expectedDiscountAmount = 1000
            
            val couponInformation = CouponScenario.productFixedCoupon(questionId = questionId, discount = expectedDiscountAmount)
            val userCoupon = UserCoupon.create(userId, couponInformation)
            
            val productCoupon = Coupon.createProductCoupon(questionId, couponInformation, userCoupon)
            val questionPayment = QuestionPayment.create(userId, order)
            When("상품에 쿠폰을 적용하면") {
                productCoupon.apply(questionPayment)
                Then("고정 금액만큼 할인이 적용된다.") {
                    val orderItem = questionPayment.getOrderItem(questionId)
                    orderItem.realPrice shouldBe originalAmount - expectedDiscountAmount
                    orderItem.appliedCoupons.size shouldBe 1
                    orderItem.appliedCoupons[0].couponInformation.title shouldBe couponInformation.title
                    orderItem.appliedCoupons[0].appliedDiscountAmount shouldBe expectedDiscountAmount
                }
            }
        }
        
        Given("퍼센트 할인 쿠폰이 있을 때") {
            val userId = 1L
            val order = QuestionOrderScenario.create(1)
            val questionId = order.items[0].questionInfo.questionId
            val originalAmount = order.items[0].priceAfterPromotion
            
            val discountPercent = 10
            val expectedDiscountAmount = originalAmount * discountPercent / 100
            
            val couponInformation = CouponScenario.productPercentCoupon(questionId = questionId, percent = discountPercent)
            val userCoupon = UserCoupon.create(userId, couponInformation)
            
            val productCoupon = Coupon.createProductCoupon(questionId, couponInformation, userCoupon)
            val questionPayment = QuestionPayment.create(userId, order)
            When("상품에 쿠폰을 적용하면") {
                productCoupon.apply(questionPayment)
                Then("정해진 퍼센트 비율만큼 할인이 적용된다.") {
                    val orderItem = questionPayment.getOrderItem(questionId)
                    orderItem.realPrice shouldBe originalAmount - expectedDiscountAmount
                    orderItem.appliedCoupons.size shouldBe 1
                    orderItem.appliedCoupons[0].couponInformation.title shouldBe couponInformation.title
                    orderItem.appliedCoupons[0].appliedDiscountAmount shouldBe expectedDiscountAmount
                }
            }
        }
        
        Given("최대 할인 금액이 정해져있는 퍼센트 할인 쿠폰의 경우") {
            val userId = 1L
            val order = QuestionOrderScenario.create(1)
            val questionId = order.items[0].questionInfo.questionId
            val originalAmount = order.items[0].priceAfterPromotion
            
            val discountPercent = 40
            val maximumDiscountAmount = 100
            
            val couponInformation =
                CouponScenario.productPercentCoupon(questionId = questionId, percent = discountPercent, maxDiscount = maximumDiscountAmount)
            val userCoupon = UserCoupon.create(userId, couponInformation)
            
            val productCoupon = Coupon.createProductCoupon(questionId, couponInformation, userCoupon)
            val questionPayment = QuestionPayment.create(userId, order)
            When("상품에 쿠폰을 적용하면") {
                productCoupon.apply(questionPayment)
                Then("최대 할인 금액까지만 할인이 적용된다.") {
                    val orderItem = questionPayment.getOrderItem(questionId)
                    orderItem.realPrice shouldBe originalAmount - maximumDiscountAmount
                    orderItem.appliedCoupons.size shouldBe 1
                    orderItem.appliedCoupons[0].appliedDiscountAmount shouldBe maximumDiscountAmount
                }
            }
        }
        
        Given("최소 금액이 정해져있는 할인 쿠폰의 경우") {
            val userId = 1L
            val discountPercent = 40
            val order = QuestionOrderScenario.create(1)
            val questionId = order.items[0].questionInfo.questionId
            val originalAmount = order.items[0].priceAfterPromotion
            
            val couponInformation =
                CouponScenario.productPercentCoupon(questionId = questionId, percent = discountPercent, minPurchase = originalAmount + 1000)
            val userCoupon = UserCoupon.create(userId, couponInformation)
            
            val productCoupon = Coupon.createProductCoupon(questionId, couponInformation, userCoupon)
            val questionPayment = QuestionPayment.create(userId, order)
            
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
