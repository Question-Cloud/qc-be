package com.eager.questioncloud.application.api.user.coupon.controller

import com.eager.questioncloud.application.api.common.DefaultResponse
import com.eager.questioncloud.application.api.common.DefaultResponse.Companion.success
import com.eager.questioncloud.application.api.user.coupon.dto.GetAvailableUserCouponsResponse
import com.eager.questioncloud.application.api.user.coupon.dto.RegisterCouponRequest
import com.eager.questioncloud.application.api.user.coupon.service.UserCouponService
import com.eager.questioncloud.application.security.UserPrincipal
import jakarta.validation.Valid
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/user/coupon")
class UserCouponController(
    private val userCouponService: UserCouponService
) {
    @GetMapping
    fun getAvailableUserCoupons(@AuthenticationPrincipal userPrincipal: UserPrincipal): GetAvailableUserCouponsResponse {
        val coupons = userCouponService.getAvailableUserCoupons(userPrincipal.user.uid)
        return GetAvailableUserCouponsResponse(coupons)
    }

    @PostMapping
    fun registerCoupon(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @RequestBody request: @Valid RegisterCouponRequest
    ): DefaultResponse {
        userCouponService.registerCoupon(userPrincipal.user.uid, request.code)
        return success()
    }
}
