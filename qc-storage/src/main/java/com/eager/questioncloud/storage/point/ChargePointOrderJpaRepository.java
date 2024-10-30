package com.eager.questioncloud.storage.point;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChargePointOrderJpaRepository extends JpaRepository<ChargePointOrderEntity, Long> {
    Optional<ChargePointOrderEntity> findByPaymentId(String paymentId);
}
