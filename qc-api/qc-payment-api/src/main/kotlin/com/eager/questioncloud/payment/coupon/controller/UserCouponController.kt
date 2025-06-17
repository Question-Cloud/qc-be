package com.eager.questioncloud.payment.coupon.controller

import com.eager.questioncloud.common.auth.UserPrincipal
import com.eager.questioncloud.common.dto.DefaultResponse
import com.eager.questioncloud.payment.coupon.dto.GetAvailableUserCouponsResponse
import com.eager.questioncloud.payment.coupon.dto.RegisterCouponRequest
import com.eager.questioncloud.payment.coupon.service.UserCouponService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/payment/coupon")
class UserCouponController(
    private val userCouponService: UserCouponService
) {
    @GetMapping
    fun getAvailableUserCoupons(userPrincipal: UserPrincipal): GetAvailableUserCouponsResponse {
        val coupons = userCouponService.getAvailableUserCoupons(userPrincipal.userId)
        return GetAvailableUserCouponsResponse(coupons)
    }

    @PostMapping
    fun registerCoupon(
        userPrincipal: UserPrincipal,
        @RequestBody request: @Valid RegisterCouponRequest
    ): DefaultResponse {
        userCouponService.registerCoupon(userPrincipal.userId, request.code)
        return DefaultResponse.success()
    }
}
