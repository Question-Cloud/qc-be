package com.eager.questioncloud.payment.domain

import kotlin.math.floor

class PercentPromotion(
    val promotionId: Long,
    val title: String,
    val value: Int,
    var appliedAmount: Int = 0,
) : Discountable {
    override fun getDiscountAmount(originAmount: Int): Int {
        val discountRate = value / 100.0
        val discountAmount = floor(originAmount * discountRate).toInt()
        appliedAmount = discountAmount
        return discountAmount
    }
    
    override fun getName(): String {
        return title
    }
    
    override fun getDiscountType(): DiscountType {
        return DiscountType.PROMOTION
    }
    
    override fun getSourceId(): Long {
        return promotionId
    }
    
    override fun getAppliedDiscountAmount(): Int {
        return appliedAmount
    }
}