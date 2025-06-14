package com.eager.questioncloud.subscribe.infrastructure.repository

import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.subscribe.domain.Subscribe
import com.eager.questioncloud.subscribe.infrastructure.entity.QSubscribeEntity.subscribeEntity
import com.eager.questioncloud.subscribe.infrastructure.entity.SubscribeEntity
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class SubscribeRepositoryImpl(
    private val subscribeJpaRepository: SubscribeJpaRepository,
    private val jpaQueryFactory: JPAQueryFactory,
) : SubscribeRepository {
    override fun save(subscribe: Subscribe): Subscribe {
        return subscribeJpaRepository.save(SubscribeEntity.from(subscribe)).toModel()
    }

    override fun isSubscribed(subscriberId: Long, creatorId: Long): Boolean {
        return subscribeJpaRepository.existsBySubscriberIdAndCreatorId(subscriberId, creatorId)
    }

    @Transactional
    override fun unSubscribe(subscriberId: Long, creatorId: Long) {
        subscribeJpaRepository.deleteBySubscriberIdAndCreatorId(subscriberId, creatorId)
    }

    override fun countSubscriber(creatorId: Long): Int {
        return subscribeJpaRepository.countByCreatorId(creatorId)
    }

    override fun countSubscriber(creatorIds: List<Long>): Map<Long, Int> {
        return jpaQueryFactory.select(subscribeEntity.creatorId, subscribeEntity.id.count().intValue())
            .from(subscribeEntity)
            .where(subscribeEntity.creatorId.`in`(creatorIds))
            .groupBy(subscribeEntity.creatorId)
            .fetch()
            .associate { it.get(subscribeEntity.creatorId)!! to it.get(subscribeEntity.id.count().intValue())!! }
    }

    override fun getMySubscribedCreators(userId: Long, pagingInformation: PagingInformation): List<Long> {
        return jpaQueryFactory.select(subscribeEntity.creatorId)
            .from(subscribeEntity)
            .where(subscribeEntity.subscriberId.eq(userId))
            .offset(pagingInformation.offset.toLong())
            .limit(pagingInformation.size.toLong())
            .fetch()
    }

    override fun countMySubscribe(userId: Long): Int {
        return jpaQueryFactory.select(subscribeEntity.id.count().intValue())
            .from(subscribeEntity)
            .where(subscribeEntity.subscriberId.eq(userId))
            .fetchFirst() ?: 0
    }

    override fun deleteAllInBatch() {
        subscribeJpaRepository.deleteAllInBatch()
    }
}
