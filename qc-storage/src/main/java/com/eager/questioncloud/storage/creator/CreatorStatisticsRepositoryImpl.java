package com.eager.questioncloud.storage.creator;

import com.eager.questioncloud.core.domain.creator.model.CreatorStatistics;
import com.eager.questioncloud.core.domain.creator.repository.CreatorStatisticsRepository;
import com.eager.questioncloud.core.exception.CustomException;
import com.eager.questioncloud.core.exception.Error;
import java.util.List;
import java.util.stream.Collectors;
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

    @Override
    public void saveAll(List<CreatorStatistics> creatorStatistics) {
        creatorStatisticsJpaRepository.saveAll(
            creatorStatistics.stream().map(CreatorStatisticsEntity::from).collect(Collectors.toList())
        );
    }

    @Override
    public CreatorStatistics findByCreatorId(Long creatorId) {
        return creatorStatisticsJpaRepository.findByCreatorId(creatorId)
            .orElseThrow(() -> new CustomException(Error.NOT_FOUND))
            .toModel();
    }
}
