package com.eager.questioncloud.core.domain.payment.model

import com.eager.questioncloud.core.domain.question.model.Question

class QuestionOrderItem(
    var id: Long = 0,
    var questionId: Long,
    var price: Int,
) {
    companion object {
        fun create(question: Question): QuestionOrderItem {
            return QuestionOrderItem(
                questionId = question.id!!,
                price = question.questionContent.price
            )
        }
    }
}