package com.eager.questioncloud.core.domain.review.model

import java.time.LocalDateTime

class QuestionReview(
    val id: Long? = null,
    val questionId: Long,
    val reviewerId: Long,
    var comment: String,
    var rate: Int,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    var isDeleted: Boolean = false,
) {
    fun modify(comment: String, newRate: Int): Int {
        val beforeRate = this.rate

        this.comment = comment
        this.rate = newRate

        return newRate - beforeRate
    }

    fun delete() {
        this.isDeleted = true
    }

    companion object {
        @JvmStatic
        fun create(
            questionId: Long,
            reviewerId: Long,
            comment: String,
            rate: Int
        ): QuestionReview {
            return QuestionReview(
                questionId = questionId,
                reviewerId = reviewerId,
                comment = comment,
                rate = rate
            )
        }
    }
}
