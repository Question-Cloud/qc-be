package com.eager.questioncloud.domain.point;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChargePointPaymentJpaRepository extends JpaRepository<ChargePointPaymentEntity, String> {
    Optional<ChargePointPaymentEntity> findByPaymentId(String paymentId);
}
