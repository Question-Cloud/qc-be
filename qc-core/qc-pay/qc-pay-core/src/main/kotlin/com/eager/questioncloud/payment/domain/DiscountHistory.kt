package com.eager.questioncloud.payment.domain

import com.eager.questioncloud.payment.enums.CouponType

data class DiscountHistory(
    val id: Long = 0,
    val couponType: CouponType? = null,
    var paymentId: Long? = null,
    var orderItemId: Long? = null,
    val discountAmount: Int,
    val name: String,
    val sourceId: Long,
) {
    fun updatePaymentId(paymentId: Long) {
        this.paymentId = paymentId
    }
}

data class SimpleDiscountHistory(
    val name: String,
    val couponType: CouponType? = null,
    val orderItemId: Long? = null,
    val discountAmount: Int,
)