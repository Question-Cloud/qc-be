package com.eager.questioncloud.creator.repository;

import com.eager.questioncloud.creator.entity.CreatorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreatorJpaRepository extends JpaRepository<CreatorEntity, Long> {
}
