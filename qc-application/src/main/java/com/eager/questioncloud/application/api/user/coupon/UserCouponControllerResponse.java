package com.eager.questioncloud.application.api.user.coupon;

import com.eager.questioncloud.domain.coupon.AvailableUserCoupon;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class UserCouponControllerResponse {
    @Getter
    @AllArgsConstructor
    public static class GetAvailableUserCouponsResponse {
        private List<AvailableUserCoupon> coupons;
    }
}
