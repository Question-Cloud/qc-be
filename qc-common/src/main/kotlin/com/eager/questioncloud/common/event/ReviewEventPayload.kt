package com.eager.questioncloud.common.event

class ReviewEventPayload(
    val questionId: Long,
    val varianceRate: Int,
    val reviewEventType: ReviewEventType
) {
    companion object {
        fun create(questionId: Long, varianceRate: Int, reviewEventType: ReviewEventType): ReviewEventPayload {
            return ReviewEventPayload(questionId, varianceRate, reviewEventType)
        }
    }
}