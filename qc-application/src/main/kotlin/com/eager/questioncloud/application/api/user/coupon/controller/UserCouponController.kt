package com.eager.questioncloud.application.api.user.coupon.controller

import com.eager.questioncloud.application.api.common.DefaultResponse
import com.eager.questioncloud.application.api.common.DefaultResponse.Companion.success
import com.eager.questioncloud.application.api.user.coupon.dto.GetAvailableUserCouponsResponse
import com.eager.questioncloud.application.api.user.coupon.dto.RegisterCouponRequest
import com.eager.questioncloud.application.api.user.coupon.service.UserCouponService
import com.eager.questioncloud.application.security.UserPrincipal
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.validation.Valid
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/user/coupon")
class UserCouponController(
    private val userCouponService: UserCouponService
) {
    @GetMapping
    @ApiResponses(value = [ApiResponse(responseCode = "200", description = "요청 성공")])
    @Operation(
        operationId = "사용 가능한 쿠폰 목록 조회",
        summary = "사용 가능한 쿠폰 목록 조회",
        tags = ["coupon"],
        description = "사용 가능한 쿠폰 목록 조회"
    )
    fun getAvailableUserCoupons(@AuthenticationPrincipal userPrincipal: UserPrincipal): GetAvailableUserCouponsResponse {
        val coupons = userCouponService.getAvailableUserCoupons(userPrincipal.user.uid)
        return GetAvailableUserCouponsResponse(coupons)
    }

    @PostMapping
    @ApiResponses(value = [ApiResponse(responseCode = "200", description = "요청 성공")])
    @Operation(operationId = "쿠폰 등록", summary = "쿠폰 등록", tags = ["coupon"], description = "쿠폰 등록")
    fun registerCoupon(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @RequestBody request: @Valid RegisterCouponRequest
    ): DefaultResponse {
        userCouponService.registerCoupon(userPrincipal.user.uid, request.code)
        return success()
    }
}
