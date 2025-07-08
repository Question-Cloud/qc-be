package com.eager.questioncloud.payment.domain

import com.eager.questioncloud.payment.enums.CouponType
import java.time.LocalDateTime

class Coupon(
    var id: Long = 0,
    var code: String,
    var title: String,
    var couponType: CouponType,
    var value: Int,
    var remainingCount: Int,
    var endAt: LocalDateTime
)
