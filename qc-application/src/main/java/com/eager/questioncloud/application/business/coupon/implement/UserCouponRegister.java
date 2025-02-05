package com.eager.questioncloud.application.business.coupon.implement;

import com.eager.questioncloud.core.domain.coupon.infrastructure.repository.CouponRepository;
import com.eager.questioncloud.core.domain.coupon.infrastructure.repository.UserCouponRepository;
import com.eager.questioncloud.core.domain.coupon.model.Coupon;
import com.eager.questioncloud.core.domain.coupon.model.UserCoupon;
import com.eager.questioncloud.core.exception.CoreException;
import com.eager.questioncloud.core.exception.Error;
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
            throw new CoreException(Error.ALREADY_REGISTER_COUPON);
        }

        if (!couponRepository.decreaseCount(coupon.getId())) {
            throw new CoreException(Error.LIMITED_COUPON);
        }

        userCouponRepository.save(UserCoupon.create(userId, coupon));
    }
}
