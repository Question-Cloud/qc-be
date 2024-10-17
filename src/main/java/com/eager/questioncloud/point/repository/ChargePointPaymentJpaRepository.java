package com.eager.questioncloud.point.repository;

import com.eager.questioncloud.point.entity.ChargePointPaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChargePointPaymentJpaRepository extends JpaRepository<ChargePointPaymentEntity, String> {
}
