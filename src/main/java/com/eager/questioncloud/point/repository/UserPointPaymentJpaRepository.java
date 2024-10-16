package com.eager.questioncloud.point.repository;

import com.eager.questioncloud.point.entity.ChargePointHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPointPaymentJpaRepository extends JpaRepository<ChargePointHistoryEntity, String> {
}
