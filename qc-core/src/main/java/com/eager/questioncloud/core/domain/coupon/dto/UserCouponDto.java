package com.eager.questioncloud.core.domain.coupon.dto;

import com.eager.questioncloud.core.domain.coupon.model.UserCoupon;
import com.eager.questioncloud.core.domain.coupon.vo.CouponType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
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

        public static List<AvailableUserCouponItem> from(List<UserCoupon> userCoupons) {
            return userCoupons.stream()
                .map(userCoupon ->
                    new AvailableUserCouponItem(
                        userCoupon.getId(),
                        userCoupon.getCoupon().getTitle(),
                        userCoupon.getCoupon().getCouponType(),
                        userCoupon.getCoupon().getValue(),
                        userCoupon.getCoupon().getEndAt())
                )
                .collect(Collectors.toList());
        }
    }
}
