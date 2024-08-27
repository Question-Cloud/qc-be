package com.eager.questioncloud.creator;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CreatorJpaRepository extends JpaRepository<CreatorEntity, Long> {
    Boolean existsByUserId(Long userId);
}
