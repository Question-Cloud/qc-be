package com.eager.questioncloud.core.domain.coupon.service;

import com.eager.questioncloud.core.common.LockKeyGenerator;
import com.eager.questioncloud.core.common.LockManager;
import com.eager.questioncloud.core.domain.coupon.dto.UserCouponDto.AvailableUserCouponItem;
import com.eager.questioncloud.core.domain.coupon.implement.UserCouponReader;
import com.eager.questioncloud.core.domain.coupon.implement.UserCouponRegister;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserCouponService {
    private final UserCouponRegister userCouponRegister;
    private final UserCouponReader userCouponReader;
    private final LockManager lockManager;

    public void registerCoupon(Long userId, String couponCode) {
        lockManager.executeWithLock(
            LockKeyGenerator.generateRegisterCouponKey(userId, couponCode),
            () -> userCouponRegister.registerCoupon(userId, couponCode));
    }

    public List<AvailableUserCouponItem> getAvailableUserCoupons(Long userId) {
        return userCouponReader.getAvailableUserCoupons(userId);
    }
}
