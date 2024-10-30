package com.eager.questioncloud.core.domain.payment.coupon.implement;

import com.eager.questioncloud.core.domain.payment.coupon.repository.CouponRepository;
import com.eager.questioncloud.core.exception.CustomException;
import com.eager.questioncloud.core.exception.Error;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CouponUpdater {
    private final CouponRepository couponRepository;

    public void decreaseCount(Long couponId) {
        if (!couponRepository.decreaseCount(couponId)) {
            throw new CustomException(Error.LIMITED_COUPON);
        }
    }
}
