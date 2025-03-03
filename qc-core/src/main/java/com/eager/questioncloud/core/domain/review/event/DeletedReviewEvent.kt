package com.eager.questioncloud.core.domain.review.event

class DeletedReviewEvent(
    val questionId: Long,
    val rate: Int,
) {
    companion object {
        @JvmStatic
        fun create(questionId: Long, rate: Int): DeletedReviewEvent {
            return DeletedReviewEvent(questionId, rate)
        }
    }
}
