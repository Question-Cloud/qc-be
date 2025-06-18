package com.eager.questioncloud.pay.coupon.dto

import jakarta.validation.constraints.NotBlank

class RegisterCouponRequest(
    @NotBlank val code: String = ""
)
