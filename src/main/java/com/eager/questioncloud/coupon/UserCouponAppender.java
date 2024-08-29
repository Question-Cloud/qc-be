package com.eager.questioncloud.coupon;

import com.eager.questioncloud.exception.CustomException;
import com.eager.questioncloud.exception.Error;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserCouponAppender {
    private final UserCouponRepository userCouponRepository;
    private final CouponReader couponReader;
    private final CouponUpdater couponUpdater;

    @Transactional
    public UserCoupon registerCoupon(Long userId, String couponCode) {
        Coupon coupon = couponReader.getCoupon(couponCode);

        if (userCouponRepository.checkDuplicate(userId, coupon.getId())) {
            throw new CustomException(Error.ALREADY_REGISTER_COUPON);
        }

        UserCoupon userCoupon = userCouponRepository.append(UserCoupon.create(userId, coupon));
        couponUpdater.decreaseCount(coupon);
        return userCoupon;
    }
}
