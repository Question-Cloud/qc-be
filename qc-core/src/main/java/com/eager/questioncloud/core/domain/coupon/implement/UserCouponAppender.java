package com.eager.questioncloud.core.domain.coupon.implement;

import com.eager.questioncloud.core.domain.coupon.model.UserCoupon;
import com.eager.questioncloud.core.domain.coupon.repository.UserCouponRepository;
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
