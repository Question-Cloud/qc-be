package com.eager.questioncloud.coupon;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CouponReader {
    private final CouponRepository couponRepository;

    public Coupon getCoupon(String code) {
        return couponRepository.getCoupon(code);
    }

    public Coupon getCoupon(Long id) {
        return couponRepository.getCoupon(id);
    }
}
