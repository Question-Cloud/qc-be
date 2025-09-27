package com.eager.questioncloud.payment.domain

import kotlin.math.floor

class PercentCoupon(
    val couponId: Long,
    val userCouponId: Long,
    val title: String,
    val value: Int,
    var appliedDiscountAmount: Int = 0,
) : Discountable {
    override fun getDiscountAmount(originAmount: Int): Int {
        val discountRate = value / 100.0
        val discountAmount = floor(originAmount * discountRate).toInt()
        appliedDiscountAmount = discountAmount
        return discountAmount
    }
    
    override fun getName(): String {
        return title
    }
    
    override fun getDiscountType(): DiscountType {
        return DiscountType.COUPON
    }
    
    override fun getSourceId(): Long {
        return userCouponId
    }
    
    override fun getAppliedDiscountAmount(): Int {
        return appliedDiscountAmount
    }
}