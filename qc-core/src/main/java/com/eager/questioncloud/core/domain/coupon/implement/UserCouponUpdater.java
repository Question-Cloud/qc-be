package com.eager.questioncloud.core.domain.coupon.implement;

import com.eager.questioncloud.core.domain.coupon.model.UserCoupon;
import com.eager.questioncloud.core.domain.coupon.repository.UserCouponRepository;
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
