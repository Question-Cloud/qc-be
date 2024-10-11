package com.eager.questioncloud.coupon.repository;

import com.eager.questioncloud.coupon.entity.UserCouponEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCouponJpaRepository extends JpaRepository<UserCouponEntity, Long> {
    Boolean existsByUserIdAndCouponId(Long userId, Long couponId);

    Optional<UserCouponEntity> findByIdAndUserIdAndIsUsedFalse(Long id, Long userId);
}