package com.eager.questioncloud.application.message

import com.eager.questioncloud.core.domain.payment.model.QuestionPayment

class FailQuestionPaymentMessage(
    var questionPayment: QuestionPayment,
    var failCount: Int,
) {
    fun increaseFailCount() {
        failCount++
    }

    companion object {
        fun create(questionPayment: QuestionPayment): FailQuestionPaymentMessage {
            return FailQuestionPaymentMessage(questionPayment, 0)
        }
    }
}
