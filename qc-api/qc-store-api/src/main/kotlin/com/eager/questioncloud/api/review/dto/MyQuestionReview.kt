package com.eager.questioncloud.api.review.dto

import com.eager.questioncloud.review.domain.QuestionReview

class MyQuestionReview(
    val id: Long,
    val rate: Int,
    val comment: String,
) {
    companion object {
        fun from(questionReview: QuestionReview): MyQuestionReview {
            return MyQuestionReview(questionReview.id, questionReview.rate, questionReview.comment)
        }
    }
}
