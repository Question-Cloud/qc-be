package com.eager.questioncloud.payment.domain

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.payment.enums.DiscountCalculationType
import kotlin.math.floor
import kotlin.math.min

class CouponPolicy(
    val couponInformation: CouponInformation,
    private val userCoupon: UserCoupon,
) : Discountable {
    override fun getDiscountAmount(originAmount: Int): Int {
        if (originAmount < couponInformation.minimumPurchaseAmount) {
            throw CoreException(Error.WRONG_COUPON)
        }
        
        val discountAmount = when (couponInformation.discountCalculationType) {
            DiscountCalculationType.FIXED -> couponInformation.value
            DiscountCalculationType.PERCENT -> floor(originAmount * couponInformation.value / 100.0).toInt()
        }
        
        return min(discountAmount, couponInformation.maximumDiscountAmount)
    }
    
    override fun getName(): String {
        return couponInformation.title
    }
    
    override fun getSourceId(): Long {
        return userCoupon.id
    }
}