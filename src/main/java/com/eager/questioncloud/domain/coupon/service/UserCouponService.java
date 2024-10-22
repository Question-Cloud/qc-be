package com.eager.questioncloud.domain.coupon.service;

import com.eager.questioncloud.domain.coupon.dto.UserCouponDto.AvailableUserCouponItem;
import com.eager.questioncloud.domain.coupon.implement.UserCouponReader;
import com.eager.questioncloud.domain.coupon.implement.UserCouponRegister;
import com.eager.questioncloud.domain.coupon.model.UserCoupon;
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
