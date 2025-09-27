package com.eager.questioncloud.payment.domain

class FixedCouponDiscount(
    val couponId: Long,
    val userCouponId: Long,
    var title: String,
    val value: Int,
) : DiscountPolicy {
    override fun getDiscountAmount(originAmount: Int): Int {
        return value
    }
    
    override fun getPolicyName(): String {
        return title
    }
    
    override fun getDiscountType(): DiscountType {
        return DiscountType.COUPON
    }
    
    override fun getSourceId(): Long {
        return userCouponId
    }
}