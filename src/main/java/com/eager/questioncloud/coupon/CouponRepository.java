package com.eager.questioncloud.coupon;

import org.springframework.stereotype.Repository;

@Repository
public interface CouponRepository {
    Coupon getCoupon(Long id);

    Coupon getCoupon(String code);

    Coupon save(Coupon coupon);

    Boolean decreaseCount(Long couponId);
}
