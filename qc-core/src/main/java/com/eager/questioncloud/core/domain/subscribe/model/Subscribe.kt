package com.eager.questioncloud.core.domain.subscribe.model

import java.time.LocalDateTime

class Subscribe(
    val id: Long? = null,
    val subscriberId: Long,
    val creatorId: Long,
    val createdAt: LocalDateTime = LocalDateTime.now(),
) {
    companion object {
        @JvmStatic
        fun create(subscriberId: Long, creatorId: Long): Subscribe {
            return Subscribe(subscriberId = subscriberId, creatorId = creatorId)
        }
    }
}
