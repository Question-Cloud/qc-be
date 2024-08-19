package com.eager.questioncloud.coupon;

import com.eager.questioncloud.exception.CustomException;
import com.eager.questioncloud.exception.Error;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CouponRepositoryImpl implements CouponRepository {
    private final CouponJpaRepository couponJpaRepository;

    @Override
    public Coupon getCoupon(String code) {
        return couponJpaRepository.findByCode(code)
            .orElseThrow(() -> new CustomException(Error.NOT_FOUND))
            .toDomain();
    }

    @Override
    public Coupon save(Coupon coupon) {
        return couponJpaRepository.save(coupon.toEntity()).toDomain();
    }
}
