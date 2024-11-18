package com.eager.questioncloud.domain.coupon.repository;

import com.eager.questioncloud.domain.coupon.model.Coupon;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRepository {
    Coupon findById(Long id);

    Coupon findByCode(String code);

    Coupon save(Coupon coupon);

    Boolean decreaseCount(Long couponId);
}
