package com.eager.questioncloud.core.domain.creator.infrastructure;

import static com.eager.questioncloud.core.domain.creator.infrastructure.QCreatorStatisticsEntity.creatorStatisticsEntity;

import com.eager.questioncloud.core.domain.creator.model.CreatorStatistics;
import com.eager.questioncloud.exception.CustomException;
import com.eager.questioncloud.exception.Error;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CreatorStatisticsRepositoryImpl implements CreatorStatisticsRepository {
    private final CreatorStatisticsJpaRepository creatorStatisticsJpaRepository;
    private final JPAQueryFactory jpaQueryFactory;

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

    @Override
    public void addSalesCount(Long creatorId, int count) {
        jpaQueryFactory.update(creatorStatisticsEntity)
            .set(creatorStatisticsEntity.salesCount, creatorStatisticsEntity.salesCount.add(count))
            .where(creatorStatisticsEntity.creatorId.eq(creatorId))
            .execute();
    }

    @Override
    public void deleteAllInBatch() {
        creatorStatisticsJpaRepository.deleteAllInBatch();
    }
}
