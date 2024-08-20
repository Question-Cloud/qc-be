package com.eager.questioncloud.coupon;

import com.eager.questioncloud.coupon.UserCouponDto.AvailableUserCouponItem;
import java.util.List;

public interface UserCouponRepository {
    UserCoupon append(UserCoupon userCoupon);

    UserCoupon getUserCoupon(Long userCouponId, Long userId);

    Boolean checkDuplicate(Long userId, Long couponId);

    UserCoupon save(UserCoupon userCoupon);

    List<AvailableUserCouponItem> getAvailableUserCoupons(Long userId);
}
