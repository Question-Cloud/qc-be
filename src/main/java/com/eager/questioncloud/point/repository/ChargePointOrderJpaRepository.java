package com.eager.questioncloud.point.repository;

import com.eager.questioncloud.point.entity.ChargePointOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChargePointOrderJpaRepository extends JpaRepository<ChargePointOrderEntity, Long> {
}
