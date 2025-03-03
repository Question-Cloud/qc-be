package com.eager.questioncloud.core.domain.subscribe.event

class UnsubscribedEvent(
    val creatorId: Long
) {
    companion object {
        @JvmStatic
        fun create(creatorId: Long): UnsubscribedEvent {
            return UnsubscribedEvent(creatorId)
        }
    }
}
