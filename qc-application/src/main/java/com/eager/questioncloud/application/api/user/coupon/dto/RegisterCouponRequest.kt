package com.eager.questioncloud.application.api.user.coupon.dto

import jakarta.validation.constraints.NotBlank

class RegisterCouponRequest(
    @NotBlank val code: String = ""
)
