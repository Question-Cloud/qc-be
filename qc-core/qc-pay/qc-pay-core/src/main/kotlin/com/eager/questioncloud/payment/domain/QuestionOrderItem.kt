package com.eager.questioncloud.payment.domain

import com.eager.questioncloud.payment.dto.QuestionInfo
import com.eager.questioncloud.payment.dto.QuestionPromotionData

class QuestionOrderItem(
    var id: Long = 0,
    val questionInfo: QuestionInfo,
) {
    val originalPrice: Int = questionInfo.price
    var realPrice: Int = originalPrice
    
    val priceAfterPromotion: Int
        get() = originalPrice - promotionDiscountAmount
    var promotionDiscountAmount: Int = 0
    var appliedPromotion: QuestionPromotionData? = null
    val appliedCoupons: MutableList<Coupon> = mutableListOf()
    
    fun applyPromotion(promotion: QuestionPromotionData) {
        promotionDiscountAmount = originalPrice - promotion.salePrice
        realPrice = promotion.salePrice
        appliedPromotion = promotion
    }
    
    fun applyDiscount(discountAmount: Int) {
        realPrice -= discountAmount
        realPrice = realPrice.coerceAtLeast(0)
    }
    
    fun stored(id: Long) {
        this.id = id
    }
}