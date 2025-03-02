package com.eager.questioncloud.core.domain.coupon.model

import com.eager.questioncloud.core.domain.coupon.enums.CouponType
import java.time.LocalDateTime

class Coupon(
    var id: Long? = null,
    var code: String,
    var title: String,
    var couponType: CouponType,
    var value: Int,
    var remainingCount: Int,
    var endAt: LocalDateTime
)
