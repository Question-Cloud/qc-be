package com.eager.questioncloud.review.domain

import java.time.LocalDateTime

class QuestionReview(
    val id: Long = 0,
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
