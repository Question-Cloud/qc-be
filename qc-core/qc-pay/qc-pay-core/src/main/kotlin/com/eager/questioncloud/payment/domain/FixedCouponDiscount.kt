package com.eager.questioncloud.payment.domain

import kotlin.math.max

class FixedCouponDiscount(
    var title: String,
    val value: Int,
) : DiscountPolicy {
    override fun discount(originAmount: Int): Int {
        return max(originAmount - value, 0)
    }
    
    override fun getPolicyName(): String {
        return title
    }
    
    override fun getDiscountValue(): Int {
        return value
    }
}