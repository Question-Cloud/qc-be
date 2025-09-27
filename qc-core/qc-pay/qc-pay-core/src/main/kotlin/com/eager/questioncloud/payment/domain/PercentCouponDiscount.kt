package com.eager.questioncloud.payment.domain

import kotlin.math.floor

class PercentCouponDiscount(
    val couponId: Long,
    val userCouponId: Long,
    val title: String,
    val value: Int,
) : DiscountPolicy {
    override fun getDiscountAmount(originAmount: Int): Int {
        val discountRate = value / 100.0
        val discountAmount = floor(originAmount * discountRate).toInt()
        return discountAmount
    }
    
    override fun getPolicyName(): String {
        return title
    }
    
    override fun getDiscountType(): DiscountType {
        return DiscountType.COUPON
    }
    
    override fun getSourceId(): Long {
        return userCouponId
    }
}