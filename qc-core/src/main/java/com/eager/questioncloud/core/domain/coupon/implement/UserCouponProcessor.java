package com.eager.questioncloud.core.domain.coupon.implement;

import com.eager.questioncloud.core.domain.coupon.model.UserCoupon;
import com.eager.questioncloud.core.domain.questionhub.payment.implement.CouponReader;
import com.eager.questioncloud.core.domain.questionhub.payment.model.Coupon;
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
