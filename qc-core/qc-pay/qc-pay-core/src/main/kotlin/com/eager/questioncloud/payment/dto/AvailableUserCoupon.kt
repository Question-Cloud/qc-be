package com.eager.questioncloud.payment.dto

import com.eager.questioncloud.payment.enums.CouponType
import java.time.LocalDateTime

data class AvailableUserCoupon(
    val id: Long,
    val title: String,
    val couponType: CouponType,
    val value: Int,
    val endAt: LocalDateTime
)
