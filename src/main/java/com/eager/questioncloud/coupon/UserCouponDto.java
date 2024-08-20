package com.eager.questioncloud.coupon;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class UserCouponDto {
    @Getter
    @AllArgsConstructor
    public static class AvailableUserCouponItem {
        private Long id;
        private String title;
        private LocalDateTime endAt;
    }
}
