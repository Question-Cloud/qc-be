package com.eager.questioncloud.core.domain.payment.coupon.service;

import com.eager.questioncloud.core.domain.payment.coupon.dto.UserCouponDto.AvailableUserCouponItem;
import com.eager.questioncloud.core.domain.payment.coupon.implement.UserCouponReader;
import com.eager.questioncloud.core.domain.payment.coupon.implement.UserCouponRegister;
import com.eager.questioncloud.core.domain.payment.coupon.model.UserCoupon;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserCouponService {
    private final UserCouponRegister userCouponRegister;
    private final UserCouponReader userCouponReader;

    public UserCoupon registerCoupon(Long userId, String couponCode) {
        return userCouponRegister.registerCoupon(userId, couponCode);
    }

    public List<AvailableUserCouponItem> getAvailableUserCoupons(Long userId) {
        return userCouponReader.getAvailableUserCoupons(userId);
    }
}
