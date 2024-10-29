package com.eager.questioncloud.core.domain.coupon.implement;

import com.eager.questioncloud.core.domain.coupon.model.Coupon;
import com.eager.questioncloud.core.domain.coupon.model.UserCoupon;
import com.eager.questioncloud.exception.CustomException;
import com.eager.questioncloud.exception.Error;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class UserCouponRegister {
    private final CouponReader couponReader;
    private final CouponUpdater couponUpdater;
    private final UserCouponReader userCouponReader;
    private final UserCouponAppender userCouponAppender;

    @Transactional
    public UserCoupon registerCoupon(Long userId, String couponCode) {
        Coupon coupon = couponReader.findByCode(couponCode);

        if (userCouponReader.isRegistered(userId, coupon.getId())) {
            throw new CustomException(Error.ALREADY_REGISTER_COUPON);
        }

        couponUpdater.decreaseCount(coupon.getId());

        return userCouponAppender.append(UserCoupon.create(userId, coupon));
    }
}
