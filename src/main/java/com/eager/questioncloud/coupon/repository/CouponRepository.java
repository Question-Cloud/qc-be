package com.eager.questioncloud.coupon.repository;

import com.eager.questioncloud.coupon.domain.Coupon;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRepository {
    Coupon getCoupon(Long id);

    Coupon getCoupon(String code);

    Coupon save(Coupon coupon);

    Boolean decreaseCount(Long couponId);
}
