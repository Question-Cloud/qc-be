package com.eager.questioncloud.coupon;

import com.eager.questioncloud.exception.CustomException;
import com.eager.questioncloud.exception.Error;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserCouponRepositoryImpl implements UserCouponRepository {
    private final UserCouponJpaRepository userCouponJpaRepository;

    @Override
    public UserCoupon append(UserCoupon userCoupon) {
        return userCouponJpaRepository.save(userCoupon.toEntity()).toModel();
    }

    @Override
    public UserCoupon getUserCoupon(Long userCouponId, Long userId) {
        return userCouponJpaRepository.findByIdAndUserIdAndIsUsedFalse(userCouponId, userId)
            .orElseThrow(() -> new CustomException(Error.WRONG_COUPON))
            .toModel();
    }

    @Override
    public Boolean checkDuplicate(Long userId, Long couponId) {
        return userCouponJpaRepository.existsByUserIdAndCouponId(userId, couponId);
    }

    @Override
    public UserCoupon save(UserCoupon userCoupon) {
        return userCouponJpaRepository.save(userCoupon.toEntity()).toModel();
    }
}
