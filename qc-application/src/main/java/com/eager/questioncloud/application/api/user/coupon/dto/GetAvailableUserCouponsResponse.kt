package com.eager.questioncloud.application.api.user.coupon.dto;

import com.eager.questioncloud.core.domain.coupon.dto.AvailableUserCoupon;
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
