package com.eager.questioncloud.coupon.dto;

import com.eager.questioncloud.coupon.model.CouponType;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class UserCouponDto {
    @Getter
    @AllArgsConstructor
    public static class AvailableUserCouponItem {
        private Long id;
        private String title;
        private CouponType couponType;
        private int value;
        private LocalDateTime endAt;
    }
}
