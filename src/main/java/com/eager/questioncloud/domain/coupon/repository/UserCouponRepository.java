package com.eager.questioncloud.domain.coupon.repository;

import com.eager.questioncloud.domain.coupon.dto.UserCouponDto.AvailableUserCouponItem;
import com.eager.questioncloud.domain.coupon.model.UserCoupon;
import java.util.List;

public interface UserCouponRepository {
    UserCoupon getUserCoupon(Long userCouponId, Long userId);

    Boolean isRegistered(Long userId, Long couponId);

    UserCoupon save(UserCoupon userCoupon);

    List<AvailableUserCouponItem> getAvailableUserCoupons(Long userId);
}
