package com.eager.questioncloud.domain.creator.repository;

import com.eager.questioncloud.domain.creator.entity.CreatorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreatorJpaRepository extends JpaRepository<CreatorEntity, Long> {
}
