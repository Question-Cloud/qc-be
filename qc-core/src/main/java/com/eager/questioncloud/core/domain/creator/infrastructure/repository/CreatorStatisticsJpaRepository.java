package com.eager.questioncloud.core.domain.creator.infrastructure.repository;

import com.eager.questioncloud.core.domain.creator.infrastructure.entity.CreatorStatisticsEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreatorStatisticsJpaRepository extends JpaRepository<CreatorStatisticsEntity, Long> {
    Optional<CreatorStatisticsEntity> findByCreatorId(Long creatorId);
}
