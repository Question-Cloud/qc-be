package com.eager.questioncloud.core.domain.coupon.infrastructure.repository;

import com.eager.questioncloud.core.domain.coupon.infrastructure.entity.CouponEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponJpaRepository extends JpaRepository<CouponEntity, Long> {
    Optional<CouponEntity> findByCode(String code);
}
