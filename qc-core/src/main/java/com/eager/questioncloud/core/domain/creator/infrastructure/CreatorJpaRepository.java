package com.eager.questioncloud.core.domain.creator.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CreatorJpaRepository extends JpaRepository<CreatorEntity, Long> {
}