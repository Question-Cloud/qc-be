package com.eager.questioncloud.domain.coupon;

import java.util.List;

public interface UserCouponRepository {
    UserCoupon getUserCoupon(Long userCouponId, Long userId);

    UserCoupon getUserCoupon(Long userCouponId);

    Boolean isRegistered(Long userId, Long couponId);

    UserCoupon save(UserCoupon userCoupon);

    List<AvailableUserCoupon> getAvailableUserCoupons(Long userId);
}
