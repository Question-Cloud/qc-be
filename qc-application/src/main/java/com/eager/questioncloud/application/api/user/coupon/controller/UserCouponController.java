package com.eager.questioncloud.application.api.user.coupon.controller;

import com.eager.questioncloud.application.api.common.DefaultResponse;
import com.eager.questioncloud.application.api.user.coupon.dto.UserCouponControllerRequest;
import com.eager.questioncloud.application.api.user.coupon.dto.UserCouponControllerResponse.GetAvailableUserCouponsResponse;
import com.eager.questioncloud.application.business.coupon.service.UserCouponService;
import com.eager.questioncloud.application.security.UserPrincipal;
import com.eager.questioncloud.core.domain.coupon.dto.AvailableUserCoupon;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user/coupon")
@RequiredArgsConstructor
public class UserCouponController {
    private final UserCouponService userCouponService;

    @GetMapping
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공")
    })
    @Operation(operationId = "사용 가능한 쿠폰 목록 조회", summary = "사용 가능한 쿠폰 목록 조회", tags = {"coupon"}, description = "사용 가능한 쿠폰 목록 조회")
    public GetAvailableUserCouponsResponse getAvailableUserCoupons(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        List<AvailableUserCoupon> coupons = userCouponService.getAvailableUserCoupons(userPrincipal.getUser().getUid());
        return new GetAvailableUserCouponsResponse(coupons);
    }

    @PostMapping
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공")
    })
    @Operation(operationId = "쿠폰 등록", summary = "쿠폰 등록", tags = {"coupon"}, description = "쿠폰 등록")
    public DefaultResponse registerCoupon(@AuthenticationPrincipal UserPrincipal userPrincipal,
        @RequestBody @Valid UserCouponControllerRequest.RegisterCouponRequest request) {
        userCouponService.registerCoupon(userPrincipal.getUser().getUid(), request.getCode());
        return DefaultResponse.success();
    }
}
