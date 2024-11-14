package com.eager.questioncloud.core.domain.creator.repository;

import com.eager.questioncloud.core.domain.creator.model.CreatorStatistics;
import java.util.List;

public interface CreatorStatisticsRepository {
    void save(CreatorStatistics creatorStatistics);

    void saveAll(List<CreatorStatistics> creatorStatistics);

    CreatorStatistics findByCreatorId(Long creatorId);
}
