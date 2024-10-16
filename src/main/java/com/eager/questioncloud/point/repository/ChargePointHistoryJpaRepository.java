package com.eager.questioncloud.point.repository;

import com.eager.questioncloud.point.entity.ChargePointHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChargePointHistoryJpaRepository extends JpaRepository<ChargePointHistoryEntity, String> {
}
