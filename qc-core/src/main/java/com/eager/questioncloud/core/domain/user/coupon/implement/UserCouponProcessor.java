package com.eager.questioncloud.core.domain.user.coupon.implement;

import com.eager.questioncloud.core.domain.hub.payment.implement.CouponReader;
import com.eager.questioncloud.core.domain.hub.payment.model.Coupon;
import com.eager.questioncloud.core.domain.user.coupon.model.UserCoupon;
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
        Coupon coupon = couponReader.findById(userCoupon.getCouponId());
        userCouponUpdater.use(userCoupon);
        return coupon;
    }
}
