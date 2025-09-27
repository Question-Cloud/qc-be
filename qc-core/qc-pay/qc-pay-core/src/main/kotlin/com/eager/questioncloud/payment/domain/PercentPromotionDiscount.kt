package com.eager.questioncloud.payment.domain

import kotlin.math.floor

class PercentPromotionDiscount(
    val promotionId: Long,
    val title: String,
    val value: Int,
    var appliedDiscountAmount: Int = 0,
) : DiscountPolicy {
    override fun getDiscountAmount(originAmount: Int): Int {
        val discountRate = value / 100.0
        val discountAmount = floor(originAmount * discountRate).toInt()
        appliedDiscountAmount = discountAmount
        return discountAmount
    }
    
    override fun getPolicyName(): String {
        return title
    }
    
    override fun getDiscountType(): DiscountType {
        return DiscountType.PROMOTION
    }
    
    override fun getSourceId(): Long {
        return promotionId
    }
    
    override fun getAppliedDiscountAmount(): Int {
        return appliedDiscountAmount
    }
}