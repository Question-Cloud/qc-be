package com.eager.questioncloud.application.api.user.coupon.dto

import com.eager.questioncloud.core.domain.coupon.dto.AvailableUserCoupon

class GetAvailableUserCouponsResponse(
    val coupons: List<AvailableUserCoupon>
)
