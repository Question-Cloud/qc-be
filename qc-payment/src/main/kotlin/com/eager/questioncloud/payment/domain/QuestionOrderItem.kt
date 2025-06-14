package com.eager.questioncloud.payment.domain

import com.eager.questioncloud.question.domain.Question

class QuestionOrderItem(
    var id: Long = 0,
    var questionId: Long,
    var price: Int,
) {
    companion object {
        fun create(question: Question): QuestionOrderItem {
            return QuestionOrderItem(
                questionId = question.id,
                price = question.questionContent.price
            )
        }
    }
}