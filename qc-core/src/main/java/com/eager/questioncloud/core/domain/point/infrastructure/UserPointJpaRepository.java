package com.eager.questioncloud.core.domain.point.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPointJpaRepository extends JpaRepository<UserPointEntity, Long> {
}
