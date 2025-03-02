package com.eager.questioncloud.core.domain.creator.infrastructure.repository;

import com.eager.questioncloud.core.domain.creator.model.CreatorStatistics;
import java.util.List;

public interface CreatorStatisticsRepository {
    void save(CreatorStatistics creatorStatistics);

    void saveAll(List<CreatorStatistics> creatorStatistics);

    CreatorStatistics findByCreatorId(Long creatorId);

    void addSalesCount(Long creatorId, int count);

    void increaseSubscribeCount(Long creatorId);

    void decreaseSubscribeCount(Long creatorId);

    void deleteAllInBatch();
}
