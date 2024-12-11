package com.eager.questioncloud.core.domain.creator;

import java.util.List;

public interface CreatorStatisticsRepository {
    void save(CreatorStatistics creatorStatistics);

    void saveAll(List<CreatorStatistics> creatorStatistics);

    CreatorStatistics findByCreatorId(Long creatorId);

    void deleteAllInBatch();
}
