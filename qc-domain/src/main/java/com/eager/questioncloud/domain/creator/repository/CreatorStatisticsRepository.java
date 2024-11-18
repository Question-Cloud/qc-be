package com.eager.questioncloud.domain.creator.repository;

import com.eager.questioncloud.domain.creator.model.CreatorStatistics;
import java.util.List;

public interface CreatorStatisticsRepository {
    void save(CreatorStatistics creatorStatistics);

    void saveAll(List<CreatorStatistics> creatorStatistics);

    CreatorStatistics findByCreatorId(Long creatorId);
}
