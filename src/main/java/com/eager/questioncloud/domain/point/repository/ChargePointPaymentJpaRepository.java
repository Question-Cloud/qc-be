package com.eager.questioncloud.domain.point.repository;

import com.eager.questioncloud.domain.point.entity.ChargePointPaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChargePointPaymentJpaRepository extends JpaRepository<ChargePointPaymentEntity, String> {
}
