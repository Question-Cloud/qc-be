package com.eager.questioncloud.coupon.implement;

import com.eager.questioncloud.coupon.model.Coupon;
import com.eager.questioncloud.coupon.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CouponReader {
    private final CouponRepository couponRepository;

    public Coupon findByCode(String code) {
        return couponRepository.findByCode(code);
    }

    public Coupon getCoupon(Long id) {
        return couponRepository.getCoupon(id);
    }
}
