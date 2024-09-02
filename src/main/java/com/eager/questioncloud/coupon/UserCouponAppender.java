package com.eager.questioncloud.coupon;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserCouponAppender {
    private final UserCouponRepository userCouponRepository;

    public UserCoupon append(UserCoupon userCoupon) {
        return userCouponRepository.append(userCoupon);
    }
}
