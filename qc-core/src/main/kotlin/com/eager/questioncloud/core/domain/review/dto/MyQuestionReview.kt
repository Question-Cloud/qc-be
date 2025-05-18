package com.eager.questioncloud.core.domain.review.dto

import com.eager.questioncloud.core.domain.review.model.QuestionReview

class MyQuestionReview(
    val id: Long,
    val rate: Int,
    val comment: String,
) {
    companion object {
        @JvmStatic
        fun from(questionReview: QuestionReview): MyQuestionReview {
            return MyQuestionReview(questionReview.id!!, questionReview.rate, questionReview.comment)
        }
    }
}
