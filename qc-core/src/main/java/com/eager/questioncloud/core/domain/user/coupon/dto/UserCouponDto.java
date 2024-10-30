package com.eager.questioncloud.core.domain.user.coupon.dto;

import com.eager.questioncloud.core.domain.hub.payment.vo.CouponType;
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
