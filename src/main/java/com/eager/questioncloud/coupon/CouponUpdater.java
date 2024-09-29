package com.eager.questioncloud.coupon;

import com.eager.questioncloud.exception.CustomException;
import com.eager.questioncloud.exception.Error;
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
