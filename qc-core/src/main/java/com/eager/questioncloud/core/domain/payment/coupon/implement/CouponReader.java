package com.eager.questioncloud.core.domain.payment.coupon.implement;

import com.eager.questioncloud.core.domain.payment.coupon.model.Coupon;
import com.eager.questioncloud.core.domain.payment.coupon.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CouponReader {
    private final CouponRepository couponRepository;

    public Coupon findByCode(String code) {
        return couponRepository.findByCode(code);
    }

    public Coupon findById(Long id) {
        return couponRepository.findById(id);
    }
}
