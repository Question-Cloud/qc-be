package com.eager.questioncloud.coupon.repository;

import com.eager.questioncloud.coupon.model.Coupon;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRepository {
    Coupon findById(Long id);

    Coupon findByCode(String code);

    Coupon save(Coupon coupon);

    Boolean decreaseCount(Long couponId);
}
