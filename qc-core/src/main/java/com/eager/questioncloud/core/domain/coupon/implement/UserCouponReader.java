package com.eager.questioncloud.core.domain.coupon.implement;

import com.eager.questioncloud.core.domain.coupon.dto.UserCouponDto.AvailableUserCouponItem;
import com.eager.questioncloud.core.domain.coupon.model.UserCoupon;
import com.eager.questioncloud.core.domain.coupon.repository.UserCouponRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserCouponReader {
    private final UserCouponRepository userCouponRepository;

    public List<AvailableUserCouponItem> getAvailableUserCoupons(Long userId) {
        List<UserCoupon> userCoupons = userCouponRepository.getUserCoupons(userId);
        return AvailableUserCouponItem.from(userCoupons);
    }

    public UserCoupon getUserCoupon(Long userId, Long userCouponId) {
        return userCouponRepository.getUserCoupon(userCouponId, userId);
    }
}
