package com.eager.questioncloud.coupon.implement;

import com.eager.questioncloud.coupon.domain.UserCoupon;
import com.eager.questioncloud.coupon.dto.UserCouponDto.AvailableUserCouponItem;
import com.eager.questioncloud.coupon.repository.UserCouponRepository;
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
