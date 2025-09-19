package com.eager.questioncloud.creator.repository

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.creator.domain.CreatorStatistics
import com.eager.questioncloud.creator.entity.CreatorStatisticsEntity
import com.eager.questioncloud.creator.entity.QCreatorStatisticsEntity.creatorStatisticsEntity
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
    
    @Transactional
    override fun incrementSubscribeCount(creatorId: Long) {
        jpaQueryFactory.update(creatorStatisticsEntity)
            .set(creatorStatisticsEntity.subscriberCount, creatorStatisticsEntity.subscriberCount.add(1))
            .where(creatorStatisticsEntity.creatorId.eq(creatorId))
            .execute()
    }
    
    @Transactional
    override fun decrementSubscribeCount(creatorId: Long) {
        jpaQueryFactory.update(creatorStatisticsEntity)
            .set(creatorStatisticsEntity.subscriberCount, creatorStatisticsEntity.subscriberCount.subtract(1))
            .where(creatorStatisticsEntity.creatorId.eq(creatorId))
            .execute()
    }
    
    override fun deleteAllInBatch() {
        creatorStatisticsJpaRepository.deleteAllInBatch()
    }
}
