package com.eager.questioncloud.core.domain.payment.coupon.implement;

import com.eager.questioncloud.core.domain.payment.coupon.model.Coupon;
import com.eager.questioncloud.core.domain.payment.coupon.model.UserCoupon;
import com.eager.questioncloud.core.domain.payment.coupon.repository.UserCouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserCouponProcessor {
    private final CouponReader couponReader;
    private final UserCouponRepository userCouponRepository;

    public Coupon useCoupon(Long userId, Long userCouponId) {
        UserCoupon userCoupon = userCouponRepository.getUserCoupon(userCouponId, userId);
        Coupon coupon = couponReader.findById(userCoupon.getCouponId());
        userCoupon.use();
        userCouponRepository.save(userCoupon);
        return coupon;
    }
}
