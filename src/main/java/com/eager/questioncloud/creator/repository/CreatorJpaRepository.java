package com.eager.questioncloud.creator.repository;

import com.eager.questioncloud.creator.entity.CreatorEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreatorJpaRepository extends JpaRepository<CreatorEntity, Long> {
    Boolean existsByUserId(Long userId);

    Optional<CreatorEntity> findByUserId(Long userId);
}
