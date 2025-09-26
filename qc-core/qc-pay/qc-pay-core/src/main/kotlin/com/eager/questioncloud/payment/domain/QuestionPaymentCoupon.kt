package com.eager.questioncloud.payment.domain

import com.eager.questioncloud.payment.enums.CouponType

class QuestionPaymentCoupon(
    private val discountPolicy: DiscountPolicy,
    val couponId: Long? = null,
    val userCouponId: Long? = null
) {
    fun calcDiscount(originalAmount: Int): Int {
        return discountPolicy.discount(originalAmount)
    }
    
    fun getCouponName(): String {
        return discountPolicy.getPolicyName()
    }
    
    companion object {
        fun create(coupon: Coupon, userCoupon: UserCoupon): QuestionPaymentCoupon {
            return when (coupon.couponType) {
                CouponType.Fixed -> QuestionPaymentCoupon(FixedCouponDiscount(coupon.title, coupon.value), coupon.id, userCoupon.id)
                CouponType.Percent -> QuestionPaymentCoupon(PercentCouponDiscount(coupon.title, coupon.value), coupon.id, userCoupon.id)
            }
        }
        
        fun noDiscount(): QuestionPaymentCoupon {
            return QuestionPaymentCoupon(NoDiscount())
        }
    }
}