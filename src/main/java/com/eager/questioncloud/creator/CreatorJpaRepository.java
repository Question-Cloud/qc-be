package com.eager.questioncloud.creator;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreatorJpaRepository extends JpaRepository<CreatorEntity, Long> {
    Boolean existsByUserId(Long userId);

    Optional<CreatorEntity> findByUserId(Long userId);
}
