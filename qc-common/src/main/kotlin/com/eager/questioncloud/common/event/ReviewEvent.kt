package com.eager.questioncloud.common.event

import io.hypersistence.tsid.TSID

class ReviewEvent(
    val questionId: Long,
    val varianceRate: Int,
    val reviewEventType: ReviewEventType
) : Event(TSID.Factory.getTsid().toString(), EventType.ReviewEvent) {
    companion object {
        fun create(questionId: Long, varianceRate: Int, reviewEventType: ReviewEventType): ReviewEvent {
            return ReviewEvent(questionId, varianceRate, reviewEventType)
        }
    }
}