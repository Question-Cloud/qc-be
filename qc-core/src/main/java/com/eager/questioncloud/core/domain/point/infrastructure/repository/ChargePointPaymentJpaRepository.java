package com.eager.questioncloud.core.domain.point.infrastructure.repository;

import com.eager.questioncloud.core.domain.point.infrastructure.entity.ChargePointPaymentEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChargePointPaymentJpaRepository extends JpaRepository<ChargePointPaymentEntity, String> {
    Optional<ChargePointPaymentEntity> findByPaymentId(String paymentId);
}
