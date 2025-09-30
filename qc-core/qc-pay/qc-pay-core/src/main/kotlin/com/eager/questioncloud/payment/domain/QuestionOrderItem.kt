package com.eager.questioncloud.payment.domain

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.payment.enums.CouponType

class QuestionOrderItem(
    var id: Long = 0,
    val questionId: Long,
    val originalPrice: Int,
    var realPrice: Int = originalPrice,
) {
    var promotionDiscountAmount: Int = 0
    val appliedCoupons: MutableList<CouponPolicy> = mutableListOf()
    var appliedPromotion: PromotionPolicy? = null
    var promotionPrice: Int = originalPrice
    
    val discountHistories: List<DiscountHistory>
        get() {
            val discountHistories = mutableListOf<DiscountHistory>()
            appliedCoupons.forEach {
                discountHistories.add(
                    DiscountHistory(
                        couponType = it.coupon.couponType,
                        orderItemId = id,
                        discountAmount = it.getDiscountAmount(realPrice),
                        name = it.getName(),
                        sourceId = it.getSourceId()
                    )
                )
            }
            return discountHistories
        }
    
    fun applyPromotion(promotion: Promotion) {
        val discountable = PromotionPolicy(promotion)
        promotionDiscountAmount = discountable.getDiscountAmount(originalPrice)
        
        realPrice = originalPrice - promotionDiscountAmount
        promotionPrice = originalPrice - promotionDiscountAmount
        
        appliedPromotion = discountable
    }
    
    fun applyCoupon(couponPolicy: CouponPolicy) {
        if (!isApplicableCoupon(couponPolicy)) throw CoreException(Error.WRONG_COUPON)
        realPrice -= couponPolicy.getDiscountAmount(promotionPrice)
        realPrice = realPrice.coerceAtLeast(0)
        appliedCoupons.add(couponPolicy)
    }
    
    fun stored(id: Long) {
        this.id = id
    }
    
    private fun isApplicableCoupon(couponPolicy: CouponPolicy): Boolean {
        return when (couponPolicy.coupon.couponType) {
            CouponType.PRODUCT_TARGET -> questionId == couponPolicy.coupon.targetQuestionId
            else -> false
        }
    }
}