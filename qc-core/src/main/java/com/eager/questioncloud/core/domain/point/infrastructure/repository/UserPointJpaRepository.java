package com.eager.questioncloud.core.domain.point.infrastructure.repository;

import com.eager.questioncloud.core.domain.point.infrastructure.entity.UserPointEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPointJpaRepository extends JpaRepository<UserPointEntity, Long> {
}