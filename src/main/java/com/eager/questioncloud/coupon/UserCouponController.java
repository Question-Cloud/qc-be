package com.eager.questioncloud.coupon;

import com.eager.questioncloud.common.DefaultResponse;
import com.eager.questioncloud.coupon.Request.RegisterCouponRequest;
import com.eager.questioncloud.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/coupon")
@RequiredArgsConstructor
public class UserCouponController {
    private final UserCouponService userCouponService;

    @PostMapping
    public DefaultResponse registerCoupon(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody RegisterCouponRequest request) {
        userCouponService.registerCoupon(userPrincipal.getUser().getUid(), request.getCode());
        return DefaultResponse.success();
    }
}
