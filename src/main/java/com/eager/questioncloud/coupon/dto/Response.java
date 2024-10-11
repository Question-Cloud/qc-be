package com.eager.questioncloud.coupon.dto;

import com.eager.questioncloud.coupon.dto.UserCouponDto.AvailableUserCouponItem;
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
