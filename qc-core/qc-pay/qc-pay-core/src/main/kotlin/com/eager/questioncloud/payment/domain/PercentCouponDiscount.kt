package com.eager.questioncloud.payment.domain

import kotlin.math.floor

class PercentCouponDiscount(
    var title: String,
    val value: Int,
) : DiscountPolicy {
    override fun discount(originAmount: Int): Int {
        val discountRate = value / 100.0
        val discountAmount = floor(originAmount * discountRate).toInt()
        return originAmount - discountAmount
    }
    
    override fun getPolicyName(): String {
        return title
    }
    
    override fun getDiscountValue(): Int {
        return value
    }
}