package com.eager.questioncloud.application.user.coupon;

import com.eager.questioncloud.domain.coupon.AvailableUserCoupon;
import com.eager.questioncloud.domain.coupon.UserCouponRepository;
import com.eager.questioncloud.lock.LockKeyGenerator;
import com.eager.questioncloud.lock.LockManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserCouponService {
    private final UserCouponRegister userCouponRegister;
    private final UserCouponRepository userCouponRepository;
    private final LockManager lockManager;

    public void registerCoupon(Long userId, String couponCode) {
        lockManager.executeWithLock(
            LockKeyGenerator.generateRegisterCouponKey(userId, couponCode),
            () -> userCouponRegister.registerCoupon(userId, couponCode));
    }

    public List<AvailableUserCoupon> getAvailableUserCoupons(Long userId) {
        return userCouponRepository.getAvailableUserCoupons(userId);
    }
}
