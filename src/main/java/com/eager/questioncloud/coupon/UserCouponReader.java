package com.eager.questioncloud.coupon;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserCouponReader {
    private final UserCouponRepository userCouponRepository;

    public UserCoupon getUserCoupon(Long userCouponId, Long userId) {
        return userCouponRepository.getUserCoupon(userCouponId, userId);
    }
}
