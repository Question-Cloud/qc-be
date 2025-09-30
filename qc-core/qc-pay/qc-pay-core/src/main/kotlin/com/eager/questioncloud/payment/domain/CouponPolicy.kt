package com.eager.questioncloud.payment.domain

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.payment.enums.DiscountCalculationType
import kotlin.math.min

class CouponPolicy(
    val coupon: Coupon,
    private val userCoupon: UserCoupon,
) : Discountable {
    private val discountCalculatePolicy: DiscountCalculatePolicy = when (coupon.discountCalculationType) {
        DiscountCalculationType.FIXED -> FixedDiscountCalculatePolicy(coupon.value)
        DiscountCalculationType.PERCENT -> PercentDiscountCalculatePolicy(coupon.value)
    }
    
    override fun getDiscountAmount(originAmount: Int): Int {
        if (originAmount < coupon.minimumPurchaseAmount) {
            throw CoreException(Error.WRONG_COUPON)
        }
        
        return min(discountCalculatePolicy.calculateDiscountAmount(originAmount), coupon.maximumDiscountAmount)
    }
    
    override fun getName(): String {
        return coupon.title
    }
    
    override fun getSourceId(): Long {
        return userCoupon.id
    }
}