package com.eager.questioncloud.core.domain.coupon.repository;

import com.eager.questioncloud.core.domain.coupon.model.UserCoupon;
import java.util.List;

public interface UserCouponRepository {
    UserCoupon getUserCoupon(Long userCouponId, Long userId);

    UserCoupon getUserCoupon(Long userCouponId);

    Boolean isRegistered(Long userId, Long couponId);

    UserCoupon save(UserCoupon userCoupon);

    List<UserCoupon> getUserCoupons(Long userId);
}
