package com.eager.questioncloud.payment.domain

import com.eager.questioncloud.payment.enums.DiscountType

class FixedPromotion(
    private val promotionId: Long,
    val title: String,
    val value: Int,
) : Discountable {
    override fun getDiscountAmount(originAmount: Int): Int {
        return value
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
}