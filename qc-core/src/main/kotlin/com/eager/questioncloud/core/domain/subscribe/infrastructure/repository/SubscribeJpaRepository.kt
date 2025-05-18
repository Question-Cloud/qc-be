package com.eager.questioncloud.core.domain.subscribe.infrastructure.repository

import com.eager.questioncloud.core.domain.subscribe.infrastructure.entity.SubscribeEntity
import org.springframework.data.jpa.repository.JpaRepository

interface SubscribeJpaRepository : JpaRepository<SubscribeEntity, Long> {
    fun existsBySubscriberIdAndCreatorId(subscriberId: Long, creatorId: Long): Boolean

    fun deleteBySubscriberIdAndCreatorId(subscriberId: Long, creatorId: Long)

    fun countByCreatorId(creatorId: Long): Int
}
