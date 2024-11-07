package com.eager.questioncloud.core.domain.payment.coupon.repository;

import com.eager.questioncloud.core.domain.payment.coupon.dto.UserCouponDto.AvailableUserCouponItem;
import com.eager.questioncloud.core.domain.payment.coupon.model.UserCoupon;
import java.util.List;

public interface UserCouponRepository {
    UserCoupon getUserCoupon(Long userCouponId, Long userId);

    UserCoupon getUserCoupon(Long userCouponId);

    Boolean isRegistered(Long userId, Long couponId);

    UserCoupon save(UserCoupon userCoupon);

    List<AvailableUserCouponItem> getAvailableUserCoupons(Long userId);
}
