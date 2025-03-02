package com.eager.questioncloud.core.domain.creator.infrastructure.repository

import com.eager.questioncloud.core.domain.creator.infrastructure.entity.CreatorStatisticsEntity.Companion.from
import com.eager.questioncloud.core.domain.creator.infrastructure.entity.QCreatorStatisticsEntity.creatorStatisticsEntity
import com.eager.questioncloud.core.domain.creator.model.CreatorStatistics
import com.eager.questioncloud.core.exception.CoreException
import com.eager.questioncloud.core.exception.Error
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.stream.Collectors

@Repository
class CreatorStatisticsRepositoryImpl(
    private val creatorStatisticsJpaRepository: CreatorStatisticsJpaRepository,
    private val jpaQueryFactory: JPAQueryFactory,
) : CreatorStatisticsRepository {
    override fun save(creatorStatistics: CreatorStatistics) {
        creatorStatisticsJpaRepository.save(from(creatorStatistics))
    }

    override fun saveAll(creatorStatistics: List<CreatorStatistics>) {
        creatorStatisticsJpaRepository.saveAll(
            creatorStatistics.stream().map { entity: CreatorStatistics -> from(entity) }.collect(Collectors.toList())
        )
    }

    override fun findByCreatorId(creatorId: Long): CreatorStatistics {
        return creatorStatisticsJpaRepository.findByCreatorId(creatorId)
            .orElseThrow { CoreException(Error.NOT_FOUND) }
            .toModel()
    }

    @Transactional
    override fun addSalesCount(creatorId: Long, count: Int) {
        jpaQueryFactory.update(creatorStatisticsEntity)
            .set(
                creatorStatisticsEntity.salesCount,
                creatorStatisticsEntity.salesCount.add(count)
            )
            .where(creatorStatisticsEntity.creatorId.eq(creatorId))
            .execute()
    }

    @Transactional
    override fun increaseSubscribeCount(creatorId: Long) {
        jpaQueryFactory.update(creatorStatisticsEntity)
            .set(
                creatorStatisticsEntity.subscribeCount,
                creatorStatisticsEntity.subscribeCount.add(1)
            )
            .where(creatorStatisticsEntity.creatorId.eq(creatorId))
            .execute()
    }

    @Transactional
    override fun decreaseSubscribeCount(creatorId: Long) {
        jpaQueryFactory.update(creatorStatisticsEntity)
            .set(
                creatorStatisticsEntity.subscribeCount,
                creatorStatisticsEntity.subscribeCount.subtract(1)
            )
            .where(creatorStatisticsEntity.creatorId.eq(creatorId))
            .execute()
    }

    override fun deleteAllInBatch() {
        creatorStatisticsJpaRepository.deleteAllInBatch()
    }
}
