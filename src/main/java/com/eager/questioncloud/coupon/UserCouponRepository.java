package com.eager.questioncloud.coupon;

public interface UserCouponRepository {
    UserCoupon append(UserCoupon userCoupon);

    UserCoupon getUserCoupon(Long userCouponId, Long userId);

    Boolean checkDuplicate(Long userId, Long couponId);

    UserCoupon save(UserCoupon userCoupon);
}
