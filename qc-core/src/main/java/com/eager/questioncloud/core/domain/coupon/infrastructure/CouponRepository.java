package com.eager.questioncloud.core.domain.coupon.infrastructure;

import com.eager.questioncloud.core.domain.coupon.model.Coupon;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRepository {
    Coupon findById(Long id);

    Coupon findByCode(String code);

    Coupon save(Coupon coupon);

    Boolean decreaseCount(Long couponId);
}
