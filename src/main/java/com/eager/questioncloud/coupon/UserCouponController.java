package com.eager.questioncloud.coupon;

import com.eager.questioncloud.common.DefaultResponse;
import com.eager.questioncloud.coupon.Request.RegisterCouponRequest;
import com.eager.questioncloud.coupon.Response.GetAvailableUserCouponsResponse;
import com.eager.questioncloud.coupon.UserCouponDto.AvailableUserCouponItem;
import com.eager.questioncloud.security.UserPrincipal;
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
@RequestMapping("/api/coupon")
@RequiredArgsConstructor
public class UserCouponController {
    private final UserCouponService userCouponService;

    @GetMapping
    public GetAvailableUserCouponsResponse getAvailableUserCoupons(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        List<AvailableUserCouponItem> coupons = userCouponService.getAvailableUserCoupons(userPrincipal.getUser().getUid());
        return new GetAvailableUserCouponsResponse(coupons);
    }

    @PostMapping
    public DefaultResponse registerCoupon(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody @Valid RegisterCouponRequest request) {
        userCouponService.registerCoupon(userPrincipal.getUser().getUid(), request.getCode());
        return DefaultResponse.success();
    }
}
