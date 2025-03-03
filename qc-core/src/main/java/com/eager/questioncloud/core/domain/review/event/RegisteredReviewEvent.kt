package com.eager.questioncloud.core.domain.review.event

class RegisteredReviewEvent(
    val questionId: Long,
    val rate: Int
) {
    companion object {
        @JvmStatic
        fun create(questionId: Long, rate: Int): RegisteredReviewEvent {
            return RegisteredReviewEvent(questionId, rate)
        }
    }
}
