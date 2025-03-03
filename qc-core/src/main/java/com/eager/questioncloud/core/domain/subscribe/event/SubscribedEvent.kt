package com.eager.questioncloud.core.domain.subscribe.event

class SubscribedEvent(
    val creatorId: Long
) {
    companion object {
        @JvmStatic
        fun create(creatorId: Long): SubscribedEvent {
            return SubscribedEvent(creatorId)
        }
    }
}
