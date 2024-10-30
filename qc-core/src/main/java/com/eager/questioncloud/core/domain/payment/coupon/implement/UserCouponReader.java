package com.eager.questioncloud.core.domain.payment.coupon.implement;

import com.eager.questioncloud.core.domain.payment.coupon.dto.UserCouponDto.AvailableUserCouponItem;
import com.eager.questioncloud.core.domain.payment.coupon.model.UserCoupon;
import com.eager.questioncloud.core.domain.payment.coupon.repository.UserCouponRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserCouponReader {
    private final UserCouponRepository userCouponRepository;

    public List<AvailableUserCouponItem> getAvailableUserCoupons(Long userId) {
        return userCouponRepository.getAvailableUserCoupons(userId);
    }

    public UserCoupon getUserCoupon(Long userCouponId, Long userId) {
        return userCouponRepository.getUserCoupon(userCouponId, userId);
    }

    public Boolean isRegistered(Long userId, Long couponId) {
        return userCouponRepository.isRegistered(userId, couponId);
    }
}