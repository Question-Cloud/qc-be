package com.eager.questioncloud.coupon;

import com.eager.questioncloud.exception.CustomException;
import com.eager.questioncloud.exception.Error;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserCouponProcessor {
    private final CouponReader couponReader;
    private final UserCouponUpdater userCouponUpdater;
    private final UserCouponReader userCouponReader;

    public int useCoupon(Long userId, Long userCouponId, int originalAmount) {
        if (originalAmount == 0) {
            throw new CustomException(Error.WRONG_COUPON);
        }

        UserCoupon userCoupon = userCouponReader.getUserCoupon(userCouponId, userId);
        Coupon coupon = couponReader.getCoupon(userCoupon.getCouponId());

        userCouponUpdater.use(userCoupon);

        return calcAmount(coupon, originalAmount);
    }

    public int calcAmount(Coupon coupon, int originalAmount) {
        if (coupon.getCouponType().equals(CouponType.Fixed)) {
            return Math.max(originalAmount - coupon.getValue(), 0);
        }
        if (coupon.getCouponType().equals(CouponType.Percent)) {
            int discountAmount = (originalAmount * (coupon.getValue() / 100));
            return originalAmount - discountAmount;
        }
        throw new CustomException(Error.WRONG_COUPON);
    }
}
