package com.eager.questioncloud.core.domain.creator.infrastructure.repository

import com.eager.questioncloud.core.domain.creator.infrastructure.entity.CreatorStatisticsEntity
import com.eager.questioncloud.core.domain.creator.infrastructure.entity.QCreatorStatisticsEntity.creatorStatisticsEntity
import com.eager.questioncloud.core.domain.creator.model.CreatorStatistics
import com.eager.questioncloud.core.exception.CoreException
import com.eager.questioncloud.core.exception.Error
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class CreatorStatisticsRepositoryImpl(
    private val creatorStatisticsJpaRepository: CreatorStatisticsJpaRepository,
    private val jpaQueryFactory: JPAQueryFactory,
) : CreatorStatisticsRepository {
    override fun save(creatorStatistics: CreatorStatistics) {
        creatorStatisticsJpaRepository.save(CreatorStatisticsEntity.createNewEntity(creatorStatistics))
    }

    override fun update(creatorStatistics: CreatorStatistics) {
        creatorStatisticsJpaRepository.save(CreatorStatisticsEntity.fromExisting(creatorStatistics))
    }

    override fun findByCreatorId(creatorId: Long): CreatorStatistics {
        return creatorStatisticsJpaRepository.findByCreatorId(creatorId)
            .orElseThrow { CoreException(Error.NOT_FOUND) }
            .toModel()
    }

    override fun getForUpdate(creatorId: Long): CreatorStatistics {
        return creatorStatisticsJpaRepository.getForUpdate(creatorId)?.toModel() ?: throw CoreException(Error.NOT_FOUND)
    }

    override fun findByCreatorIdIn(creatorIds: List<Long>): Map<Long, CreatorStatistics> {
        return creatorStatisticsJpaRepository.findByCreatorIdIn(creatorIds)
            .stream()
            .map { entity -> entity.toModel() }
            .toList()
            .associateBy { it.creatorId }
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

    override fun deleteAllInBatch() {
        creatorStatisticsJpaRepository.deleteAllInBatch()
    }
}
