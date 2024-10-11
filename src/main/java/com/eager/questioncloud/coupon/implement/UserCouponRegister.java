package com.eager.questioncloud.coupon.implement;

import com.eager.questioncloud.coupon.domain.Coupon;
import com.eager.questioncloud.coupon.domain.UserCoupon;
import com.eager.questioncloud.exception.CustomException;
import com.eager.questioncloud.exception.Error;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserCouponRegister {
    private final CouponReader couponReader;
    private final CouponUpdater couponUpdater;
    private final UserCouponReader userCouponReader;
    private final UserCouponAppender userCouponAppender;

    @Transactional
    public UserCoupon registerCoupon(Long userId, String couponCode) {
        Coupon coupon = couponReader.getCoupon(couponCode);

        if (userCouponReader.isRegistered(userId, coupon.getId())) {
            throw new CustomException(Error.ALREADY_REGISTER_COUPON);
        }

        couponUpdater.decreaseCount(coupon.getId());

        return userCouponAppender.append(UserCoupon.create(userId, coupon));
    }
}
