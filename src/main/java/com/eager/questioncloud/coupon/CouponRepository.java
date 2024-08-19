package com.eager.questioncloud.coupon;

import org.springframework.stereotype.Repository;

@Repository
public interface CouponRepository {
    Coupon getCoupon(String code);

    Coupon save(Coupon coupon);
}
