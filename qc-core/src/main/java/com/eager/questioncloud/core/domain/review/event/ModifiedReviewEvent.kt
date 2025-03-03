package com.eager.questioncloud.core.domain.review.event

class ModifiedReviewEvent(
    val questionId: Long,
    val varianceRate: Int,
) {
    companion object {
        @JvmStatic
        fun create(questionId: Long, varianceRate: Int): ModifiedReviewEvent {
            return ModifiedReviewEvent(questionId, varianceRate)
        }
    }
}
