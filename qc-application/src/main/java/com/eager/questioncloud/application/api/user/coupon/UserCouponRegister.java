package com.eager.questioncloud.application.api.user.coupon;

import com.eager.questioncloud.domain.coupon.Coupon;
import com.eager.questioncloud.domain.coupon.CouponRepository;
import com.eager.questioncloud.domain.coupon.UserCoupon;
import com.eager.questioncloud.domain.coupon.UserCouponRepository;
import com.eager.questioncloud.exception.CustomException;
import com.eager.questioncloud.exception.Error;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class UserCouponRegister {
    private final CouponRepository couponRepository;
    private final UserCouponRepository userCouponRepository;

    @Transactional
    public void registerCoupon(Long userId, String couponCode) {
        Coupon coupon = couponRepository.findByCode(couponCode);

        if (userCouponRepository.isRegistered(userId, coupon.getId())) {
            throw new CustomException(Error.ALREADY_REGISTER_COUPON);
        }

        if (!couponRepository.decreaseCount(coupon.getId())) {
            throw new CustomException(Error.LIMITED_COUPON);
        }

        userCouponRepository.save(UserCoupon.create(userId, coupon));
    }
}
