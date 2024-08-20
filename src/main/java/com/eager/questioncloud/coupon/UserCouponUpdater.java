package com.eager.questioncloud.coupon;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserCouponUpdater {
    private final UserCouponRepository userCouponRepository;

    public void use(UserCoupon userCoupon) {
        userCoupon.use();
        userCouponRepository.save(userCoupon);
    }
}
