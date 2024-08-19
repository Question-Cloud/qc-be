package com.eager.questioncloud.coupon;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CouponUpdater {
    private final CouponRepository couponRepository;

    public void decreaseCount(Coupon coupon) {
        coupon.decrease();
        couponRepository.save(coupon);
    }
}
