package com.eager.questioncloud.core.domain.subscribe.model

import java.time.LocalDateTime

class Subscribe(
    val id: Long = 0,
    val subscriberId: Long,
    val creatorId: Long,
    val createdAt: LocalDateTime = LocalDateTime.now(),
) {
    companion object {
        fun create(subscriberId: Long, creatorId: Long): Subscribe {
            return Subscribe(subscriberId = subscriberId, creatorId = creatorId)
        }
    }
}
