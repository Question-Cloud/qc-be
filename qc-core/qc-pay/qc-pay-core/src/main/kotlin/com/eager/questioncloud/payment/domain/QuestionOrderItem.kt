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
    private val appliedCouponsWithAmount: MutableList<Pair<CouponPolicy, Int>> = mutableListOf()
    private val priceAfterPromotion: Int
        get() = originalPrice - promotionDiscountAmount
    
    var promotionDiscountAmount: Int = 0
    var appliedPromotion: Promotion? = null
    val orderDiscountHistories: List<DiscountHistory>
        get() {
            return appliedCouponsWithAmount.map { (policy, amount) ->
                DiscountHistory(
                    couponType = policy.coupon.couponType,
                    orderItemId = id,
                    discountAmount = amount,
                    name = policy.getName(),
                    sourceId = policy.getSourceId()
                )
            }
        }
    
    fun applyPromotion(promotion: Promotion) {
        promotionDiscountAmount = originalPrice - promotion.salePrice
        realPrice = promotion.salePrice
        appliedPromotion = promotion
    }
    
    fun applyCoupon(couponPolicy: CouponPolicy) {
        if (!isApplicableCoupon(couponPolicy)) throw CoreException(Error.WRONG_COUPON)
        val discountAmount = couponPolicy.getDiscountAmount(priceAfterPromotion)
        realPrice -= discountAmount
        realPrice = realPrice.coerceAtLeast(0)
        appliedCouponsWithAmount.add(couponPolicy to discountAmount)
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