package com.eager.questioncloud.coupon.repository;

import com.eager.questioncloud.coupon.entity.CouponEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponJpaRepository extends JpaRepository<CouponEntity, Long> {
    Optional<CouponEntity> findByCode(String code);
}
