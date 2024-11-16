package com.eager.questioncloud.storage.creator;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreatorStatisticsJpaRepository extends JpaRepository<CreatorStatisticsEntity, Long> {
    Optional<CreatorStatisticsEntity> findByCreatorId(Long creatorId);
}