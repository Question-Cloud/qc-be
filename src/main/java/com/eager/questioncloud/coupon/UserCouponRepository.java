package com.eager.questioncloud.coupon;

public interface UserCouponRepository {
    UserCoupon append(UserCoupon userCoupon);

    Boolean checkDuplicate(Long userId, Long couponId);
}
