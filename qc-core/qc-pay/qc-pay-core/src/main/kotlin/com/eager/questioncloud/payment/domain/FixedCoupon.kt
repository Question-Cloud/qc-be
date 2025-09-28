package com.eager.questioncloud.payment.domain

import com.eager.questioncloud.payment.enums.DiscountType

class FixedCoupon(
    val couponId: Long,
    val userCouponId: Long,
    var title: String,
    val value: Int,
) : Discountable {
    override fun getDiscountAmount(originAmount: Int): Int {
        return value
    }
    
    override fun getName(): String {
        return title
    }
    
    override fun getDiscountType(): DiscountType {
        return DiscountType.COUPON
    }
    
    override fun getSourceId(): Long {
        return userCouponId
    }
}