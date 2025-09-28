package com.eager.questioncloud.payment.domain

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