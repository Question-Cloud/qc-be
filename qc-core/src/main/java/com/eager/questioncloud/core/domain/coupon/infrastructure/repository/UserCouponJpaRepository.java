package com.eager.questioncloud.core.domain.coupon.infrastructure.repository;

import com.eager.questioncloud.core.domain.coupon.infrastructure.entity.UserCouponEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCouponJpaRepository extends JpaRepository<UserCouponEntity, Long> {
    Boolean existsByUserIdAndCouponId(Long userId, Long couponId);

    Optional<UserCouponEntity> findByIdAndUserIdAndIsUsedFalse(Long id, Long userId);
}
