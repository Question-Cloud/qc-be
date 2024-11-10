package com.eager.questioncloud.api.user.coupon;

import com.eager.questioncloud.core.domain.payment.coupon.dto.UserCouponDto.AvailableUserCouponItem;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class Response {
    @Getter
    @AllArgsConstructor
    public static class GetAvailableUserCouponsResponse {
        private List<AvailableUserCouponItem> coupons;
    }
}
