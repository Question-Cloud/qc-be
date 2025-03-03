package com.eager.questioncloud.core.domain.subscribe.infrastructure.repository

import com.eager.questioncloud.core.common.PagingInformation
import com.eager.questioncloud.core.domain.creator.infrastructure.entity.QCreatorEntity.creatorEntity
import com.eager.questioncloud.core.domain.creator.infrastructure.entity.QCreatorStatisticsEntity.creatorStatisticsEntity
import com.eager.questioncloud.core.domain.subscribe.dto.SubscribeDetail
import com.eager.questioncloud.core.domain.subscribe.infrastructure.entity.QSubscribeEntity.subscribeEntity
import com.eager.questioncloud.core.domain.subscribe.infrastructure.entity.SubscribeEntity.Companion.from
import com.eager.questioncloud.core.domain.subscribe.model.Subscribe
import com.eager.questioncloud.core.domain.user.infrastructure.entity.QUserEntity.userEntity
import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class SubscribeRepositoryImpl(
    private val subscribeJpaRepository: SubscribeJpaRepository,
    private val jpaQueryFactory: JPAQueryFactory,
) : SubscribeRepository {
    override fun save(subscribe: Subscribe): Subscribe {
        return subscribeJpaRepository.save(from(subscribe)).toModel()
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

    override fun getMySubscribes(userId: Long, pagingInformation: PagingInformation): List<SubscribeDetail> {
        return jpaQueryFactory.select(
            Projections.constructor(
                SubscribeDetail::class.java,
                creatorEntity.id,
                userEntity.userInformationEntity.name,
                userEntity.userInformationEntity.profileImage,
                creatorEntity.creatorProfileEntity.mainSubject,
                creatorEntity.creatorProfileEntity.introduction,
                creatorStatisticsEntity.subscribeCount
            )
        )
            .from(subscribeEntity)
            .where(subscribeEntity.subscriberId.eq(userId))
            .leftJoin(creatorEntity).on(creatorEntity.id.eq(subscribeEntity.creatorId))
            .leftJoin(userEntity).on(userEntity.uid.eq(creatorEntity.userId))
            .leftJoin(creatorStatisticsEntity).on(creatorStatisticsEntity.creatorId.eq(creatorEntity.id))
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
}
