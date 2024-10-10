package com.eager.questioncloud.coupon.implement;

import com.eager.questioncloud.coupon.domain.UserCoupon;
import com.eager.questioncloud.coupon.repository.UserCouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserCouponAppender {
    private final UserCouponRepository userCouponRepository;

    public UserCoupon append(UserCoupon userCoupon) {
        return userCouponRepository.save(userCoupon);
    }
}
