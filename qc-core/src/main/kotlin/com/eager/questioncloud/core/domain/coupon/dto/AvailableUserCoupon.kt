package com.eager.questioncloud.core.domain.coupon.dto

import com.eager.questioncloud.core.domain.coupon.enums.CouponType
import java.time.LocalDateTime

class AvailableUserCoupon(
    val id: Long,
    val title: String,
    val couponType: CouponType,
    val value: Int,
    val endAt: LocalDateTime
) {

}
