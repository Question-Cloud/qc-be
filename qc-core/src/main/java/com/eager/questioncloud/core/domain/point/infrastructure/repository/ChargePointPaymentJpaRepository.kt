package com.eager.questioncloud.core.domain.point.infrastructure.repository;

import com.eager.questioncloud.core.domain.point.infrastructure.entity.ChargePointPaymentEntity;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

public interface ChargePointPaymentJpaRepository extends JpaRepository<ChargePointPaymentEntity, String> {
    Optional<ChargePointPaymentEntity> findByPaymentId(String paymentId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from ChargePointPaymentEntity c where c.paymentId = :paymentId")
    Optional<ChargePointPaymentEntity> findByPaymentIdWithLock(String paymentId);
}
