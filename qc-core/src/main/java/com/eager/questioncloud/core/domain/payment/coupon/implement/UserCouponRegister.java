package com.eager.questioncloud.core.domain.payment.coupon.implement;

import com.eager.questioncloud.core.domain.payment.coupon.model.Coupon;
import com.eager.questioncloud.core.domain.payment.coupon.model.UserCoupon;
import com.eager.questioncloud.core.domain.payment.coupon.repository.UserCouponRepository;
import com.eager.questioncloud.core.exception.CustomException;
import com.eager.questioncloud.core.exception.Error;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class UserCouponRegister {
    private final CouponReader couponReader;
    private final CouponUpdater couponUpdater;
    private final UserCouponRepository userCouponRepository;

    @Transactional
    public UserCoupon registerCoupon(Long userId, String couponCode) {
        Coupon coupon = couponReader.findByCode(couponCode);

        if (userCouponRepository.isRegistered(userId, coupon.getId())) {
            throw new CustomException(Error.ALREADY_REGISTER_COUPON);
        }

        couponUpdater.decreaseCount(coupon.getId());

        return userCouponRepository.save(UserCoupon.create(userId, coupon));
    }
}
