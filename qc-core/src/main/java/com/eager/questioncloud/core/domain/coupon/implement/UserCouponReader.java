package com.eager.questioncloud.core.domain.coupon.implement;

import com.eager.questioncloud.core.domain.coupon.dto.AvailableUserCoupon;
import com.eager.questioncloud.core.domain.coupon.repository.UserCouponRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserCouponReader {
    private final UserCouponRepository userCouponRepository;

    public List<AvailableUserCoupon> getAvailableUserCoupons(Long userId) {
        return userCouponRepository.getAvailableUserCoupons(userId);
    }
}
