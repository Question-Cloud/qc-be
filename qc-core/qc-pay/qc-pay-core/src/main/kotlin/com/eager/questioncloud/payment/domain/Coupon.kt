package com.eager.questioncloud.payment.domain

import com.eager.questioncloud.payment.enums.CouponType
import java.time.LocalDateTime

class Coupon(
    val id: Long = 0,
    val code: String,
    val title: String,
    val couponType: CouponType,
    val value: Int,
    val remainingCount: Int,
    val endAt: LocalDateTime
)
