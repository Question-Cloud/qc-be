package com.eager.questioncloud.coupon.domain

import com.eager.questioncloud.coupon.enums.CouponType
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
