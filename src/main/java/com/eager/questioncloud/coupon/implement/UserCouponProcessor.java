package com.eager.questioncloud.coupon.implement;

import com.eager.questioncloud.coupon.domain.Coupon;
import com.eager.questioncloud.coupon.domain.UserCoupon;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserCouponProcessor {
    private final CouponReader couponReader;
    private final UserCouponUpdater userCouponUpdater;
    private final UserCouponReader userCouponReader;

    public Coupon useCoupon(Long userId, Long userCouponId) {
        UserCoupon userCoupon = userCouponReader.getUserCoupon(userCouponId, userId);
        Coupon coupon = couponReader.getCoupon(userCoupon.getCouponId());
        userCouponUpdater.use(userCoupon);
        return coupon;
    }
}
