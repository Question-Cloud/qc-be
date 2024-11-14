package com.eager.questioncloud.core.domain.coupon.implement;

import com.eager.questioncloud.core.domain.coupon.repository.UserCouponRepository;
import com.eager.questioncloud.core.domain.coupon.dto.UserCouponDto.AvailableUserCouponItem;
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
}
