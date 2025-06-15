package com.eager.questioncloud.payment.coupon.dto

import jakarta.validation.constraints.NotBlank

class RegisterCouponRequest(
    @NotBlank val code: String = ""
)
