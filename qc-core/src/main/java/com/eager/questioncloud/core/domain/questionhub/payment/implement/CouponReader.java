package com.eager.questioncloud.core.domain.questionhub.payment.implement;

import com.eager.questioncloud.core.domain.questionhub.payment.model.Coupon;
import com.eager.questioncloud.core.domain.questionhub.payment.repository.CouponRepository;
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
