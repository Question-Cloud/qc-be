package com.eager.questioncloud.coupon;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserCouponService {
    private final UserCouponCreator userCouponCreator;

    public UserCoupon registerCoupon(Long userId, String couponCode) {
        return userCouponCreator.registerCoupon(userId, couponCode);
    }
}
