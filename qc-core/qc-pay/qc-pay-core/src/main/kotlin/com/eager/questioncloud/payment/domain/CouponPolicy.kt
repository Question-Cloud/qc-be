package com.eager.questioncloud.payment.domain

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.payment.enums.DiscountCalculationType
import kotlin.math.floor
import kotlin.math.min

class CouponPolicy(
    val coupon: Coupon,
    private val userCoupon: UserCoupon,
) : Discountable {
    override fun getDiscountAmount(originAmount: Int): Int {
        if (originAmount < coupon.minimumPurchaseAmount) {
            throw CoreException(Error.WRONG_COUPON)
        }
        
        val discountAmount = when (coupon.discountCalculationType) {
            DiscountCalculationType.FIXED -> coupon.value
            DiscountCalculationType.PERCENT -> floor(originAmount * coupon.value / 100.0).toInt()
        }
        
        return min(discountAmount, coupon.maximumDiscountAmount)
    }
    
    override fun getName(): String {
        return coupon.title
    }
    
    override fun getSourceId(): Long {
        return userCoupon.id
    }
}