package com.eager.questioncloud.domain.point.repository;

import com.eager.questioncloud.domain.point.entity.ChargePointOrderEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChargePointOrderJpaRepository extends JpaRepository<ChargePointOrderEntity, Long> {
    Optional<ChargePointOrderEntity> findByPaymentId(String paymentId);
}
