package com.eager.questioncloud.payment.domain

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.payment.dto.QuestionInfo
import com.eager.questioncloud.payment.enums.CouponType

class QuestionOrderItem(
    var id: Long = 0,
    val questionInfo: QuestionInfo,
    val originalPrice: Int = questionInfo.price,
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
                    couponType = policy.couponInformation.couponType,
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
        return when (couponPolicy.couponInformation.couponType) {
            CouponType.PRODUCT_TARGET -> questionInfo.questionId == couponPolicy.couponInformation.targetQuestionId
            else -> false
        }
    }
}