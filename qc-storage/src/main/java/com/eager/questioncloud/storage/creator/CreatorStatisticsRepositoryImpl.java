package com.eager.questioncloud.storage.creator;

import com.eager.questioncloud.core.domain.creator.model.CreatorStatistics;
import com.eager.questioncloud.core.domain.creator.repository.CreatorStatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CreatorStatisticsRepositoryImpl implements CreatorStatisticsRepository {
    private final CreatorStatisticsJpaRepository creatorStatisticsJpaRepository;

    @Override
    public void save(CreatorStatistics creatorStatistics) {
        creatorStatisticsJpaRepository.save(CreatorStatisticsEntity.from(creatorStatistics));
    }
}
