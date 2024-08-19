package com.eager.questioncloud.coupon;

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
    public Boolean checkDuplicate(Long userId, Long couponId) {
        return userCouponJpaRepository.existsByUserIdAndCouponId(userId, couponId);
    }
}
