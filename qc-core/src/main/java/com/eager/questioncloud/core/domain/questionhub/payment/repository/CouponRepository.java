package com.eager.questioncloud.core.domain.questionhub.payment.repository;

import com.eager.questioncloud.core.domain.questionhub.payment.model.Coupon;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRepository {
    Coupon findById(Long id);

    Coupon findByCode(String code);

    Coupon save(Coupon coupon);

    Boolean decreaseCount(Long couponId);
}
