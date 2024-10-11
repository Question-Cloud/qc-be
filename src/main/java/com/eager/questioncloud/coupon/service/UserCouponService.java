package com.eager.questioncloud.coupon.service;

import com.eager.questioncloud.coupon.domain.UserCoupon;
import com.eager.questioncloud.coupon.dto.UserCouponDto.AvailableUserCouponItem;
import com.eager.questioncloud.coupon.implement.UserCouponReader;
import com.eager.questioncloud.coupon.implement.UserCouponRegister;
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