package com.eager.questioncloud.core.domain.payment.coupon.implement;

import com.eager.questioncloud.core.domain.payment.coupon.dto.UserCouponDto.AvailableUserCouponItem;
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
}
